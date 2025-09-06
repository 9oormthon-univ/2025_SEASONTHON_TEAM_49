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
}