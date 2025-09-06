package org.chanme.be.kiosk.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.chanme.be.kiosk.domain.item.Itemdto.OptionGroupResponseDTO;
import org.chanme.be.kiosk.domain.item.Itemdto.SimpleItemDTO;
import org.chanme.be.kiosk.domain.KioskType;
import org.chanme.be.kiosk.domain.qusetion.questiondto.QuestionResponseDTO;
import org.chanme.be.kiosk.domain.orderdto.CartDTO;
import org.chanme.be.kiosk.domain.orderdto.OrderCheckResultDTO;
import org.chanme.be.kiosk.domain.orderdto.OrderItemDTO;
import org.chanme.be.kiosk.domain.orderdto.OrderRequestDTO;
import org.chanme.be.kiosk.service.KioskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class KioskController {

    private final KioskService kioskService;

    public KioskController(KioskService checkService) {
        this.kioskService = checkService;
    }


    /**
     * 장바구니에 아이템 1개를 추가합니다.
     * 세션이 없으면 새로 생성하고, 있으면 기존 세션에 아이템을 추가합니다.
     * POST /api/cart/items
     */

    /*
    * Item의 메뉴와 옵션이 모두 같으면 기존 장바구니(세션에 담은거에) 같은거를 +1을 추가한다. 수정 요구사항3
     */
    @PostMapping("/cart/items")
    public ResponseEntity<String> addItemToCart(
            @RequestBody OrderItemDTO itemDTO,
            HttpServletRequest request
    ) {
        // 1. 세션을 가져오거나 새로 생성합니다. (true)
        HttpSession session = request.getSession(true);

        // 2. 세션에서 'cart'라는 이름의 객체를 가져옵니다.
        CartDTO cart = (CartDTO) session.getAttribute("cart");

        // 3. 카트가 없으면(첫 아이템 추가 시) 새로 만들어 세션에 저장합니다.
        if (cart == null) {
            cart = new CartDTO();
        }

        // 4. 카트에 새로운 아이템을 추가합니다.
        cart.addItem(itemDTO);

        // 5. 변경된 카트 객체를 다시 세션에 저장합니다.
        session.setAttribute("cart", cart);

        return ResponseEntity.ok("아이템이 장바구니에 추가되었습니다.");
    }

    /**
     * 최종 주문을 확인합니다.
     * 세션에 저장된 장바구니 정보를 이용해 정답을 비교합니다.
     * POST /api/check/{questionId}
     */
    @PostMapping("/check/{questionId}")
    public ResponseEntity<OrderCheckResultDTO> checkOrder(
            @PathVariable("questionId") Long questionId,
            @RequestBody OrderRequestDTO finalOrderInfo, // 포장, 결제방식 등 최종 정보
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false); // 기존 세션 가져오기 (없으면 null)

        if (session == null || session.getAttribute("cart") == null) {
            // 세션이나 카트가 없으면 주문할 아이템이 없는 것이므로 에러 처리
            return ResponseEntity.badRequest().body(null); // 혹은 적절한 에러 DTO 반환
        }

        CartDTO cart = (CartDTO) session.getAttribute("cart");

        // CartDTO의 아이템 리스트를 기존 OrderRequestDTO에 설정
        finalOrderInfo.setItems(cart.getItems());

        // ⭐ 기존의 서비스 로직을 그대로 재사용!
        OrderCheckResultDTO result = kioskService.checkOrder(questionId, finalOrderInfo);

        // 정답 비교가 끝났으므로 세션의 장바구니를 비워줍니다.
        session.removeAttribute("cart");

        return ResponseEntity.ok(result);
    }


    /**
     * 장바구니에서 특정 아이템을 삭제합니다. (수정 요구사항1)
     * DELETE /api/cart/items/{index}
     *
     * @param index 장바구니 리스트에서 삭제할 아이템의 순서 (0부터 시작)
     */
    @DeleteMapping("/cart/items/{index}")
    public ResponseEntity<String> removeCartItem(
            @PathVariable("index") int index,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("cart") == null) {
            return ResponseEntity.badRequest().body("장바구니가 비어있습니다.");
        }

        CartDTO cart = (CartDTO) session.getAttribute("cart");
        List<OrderItemDTO> items = cart.getItems();

        // index가 유효한 범위 내에 있는지 확인
        if (index < 0 || index >= items.size()) {
            return ResponseEntity.badRequest().body("잘못된 아이템 인덱스입니다.");
        }

        // 해당 인덱스의 아이템을 리스트에서 삭제
        items.remove(index);

        // 변경된 카트를 다시 세션에 저장
        session.setAttribute("cart", cart);

        return ResponseEntity.ok("아이템이 장바구니에서 삭제되었습니다.");
    }


    /**
     * 장바구니 아이템 수량을 1씩 증가시키거나 감소시킵니다. (더 간단한 방식)
     * POST /api/cart/items/{index}/quantity?action=increase
     * POST /api/cart/items/{index}/quantity?action=decrease
     *
     * @param index 장바구니 리스트에서의 아이템 순서
     * @param action 'increase' 또는 'decrease' 값을 가짐
     */
    @PostMapping("/cart/items/{index}/quantity")
    public ResponseEntity<String> adjustCartItemQuantity(
            @PathVariable("index") int index,
            @RequestParam("action") String action,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("cart") == null) {
            return ResponseEntity.badRequest().body("장바구니가 비어있습니다.");
        }

        CartDTO cart = (CartDTO) session.getAttribute("cart");
        List<OrderItemDTO> items = cart.getItems();

        if (index < 0 || index >= items.size()) {
            return ResponseEntity.badRequest().body("잘못된 아이템 인덱스입니다.");
        }

        OrderItemDTO itemToUpdate = items.get(index);
        int currentQuantity = itemToUpdate.getQuantity();

        if ("increase".equalsIgnoreCase(action)) {
            itemToUpdate.setQuantity(currentQuantity + 1);
        } else if ("decrease".equalsIgnoreCase(action)) {
            // 수량이 1보다 클 때만 감소시킴
            if (currentQuantity > 1) {
                itemToUpdate.setQuantity(currentQuantity - 1);
            } else {
                // 수량이 1일 때는 더 이상 줄일 수 없다고 알려주거나, 아무 동작도 하지 않음
                return ResponseEntity.badRequest().body("수량은 1보다 작을 수 없습니다.");
            }
        } else {
            return ResponseEntity.badRequest().body("잘못된 action 값입니다. 'increase' 또는 'decrease'를 사용하세요.");
        }

        session.setAttribute("cart", cart);

        return ResponseEntity.ok("수량이 변경되었습니다.");
    }





    /**
     * 키오스크 타입을 받아 해당하는 랜덤 문제를 반환합니다.
     * GET /api/questions/random?type=CAFE
     */
    @GetMapping("/questions/random")
    public ResponseEntity<QuestionResponseDTO> getRandomQuestion(
            @RequestParam("type") KioskType kioskType
    ) {
        QuestionResponseDTO question = kioskService.getRandomQuestion(kioskType);
        return ResponseEntity.ok(question);
    }


    /**
     * 카테고리 ID로 해당 아이템 목록을 조회합니다.
     *
     * GET /api/item/{categoryId}
     */
    @GetMapping("/item/{categoryId}")
    public ResponseEntity<List<SimpleItemDTO>> getItemsByCategory(
            @PathVariable("categoryId") Long categoryId
    ) {
        List<SimpleItemDTO> items = kioskService.getSimpleItemsByCategory(categoryId);
        return ResponseEntity.ok(items);
    }


    /**
     * 프론트에서 메뉴 사진을 클릭하면 호출됩니다.
     * 해당 itemId에 연결된 옵션 그룹과 옵션들을 반환합니다.
     *
     * GET /api/{itemId}/options
     */
    @GetMapping("/{itemId}/options")
    public ResponseEntity<List<OptionGroupResponseDTO>> getOptionsByItem(
            @PathVariable("itemId") Long itemId
    ) {
        List<OptionGroupResponseDTO> options = kioskService.getOptionsByItem(itemId);
        return ResponseEntity.ok(options);
    }

    /**
     * 현재 세션에 저장된 장바구니의 모든 내용을 조회합니다. (테스트용)
     * GET /api/cart
     */
    @GetMapping("/cart")
    public ResponseEntity<CartDTO> getCart(HttpServletRequest request) {
        // 현재 세션을 가져옵니다 (없으면 null 반환).
        HttpSession session = request.getSession(false);

        // 세션이 없거나 세션에 'cart' 속성이 없으면,
        // 비어있는 새로운 CartDTO 객체를 반환합니다.
        if (session == null || session.getAttribute("cart") == null) {
            return ResponseEntity.ok(new CartDTO()); // { "items": [] } 형태의 비어있는 응답
        }

        // 세션에서 CartDTO 객체를 가져와서 반환합니다.
        CartDTO cart = (CartDTO) session.getAttribute("cart");
        return ResponseEntity.ok(cart);
    }



}