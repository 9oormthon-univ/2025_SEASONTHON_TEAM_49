package org.chanme.be.kiosk.service;


import org.chanme.be.kiosk.domain.item.Itemdto.OptionGroupResponseDTO;
import org.chanme.be.kiosk.domain.item.Itemdto.SimpleItemDTO;
import org.chanme.be.kiosk.domain.item.Item;
import org.chanme.be.kiosk.domain.KioskType;
import org.chanme.be.kiosk.domain.option.OptionGroup;
import org.chanme.be.kiosk.domain.orderdto.MismatchType;
import org.chanme.be.kiosk.domain.orderdto.OrderCheckResultDTO;
import org.chanme.be.kiosk.domain.orderdto.OrderItemDTO;
import org.chanme.be.kiosk.domain.orderdto.OrderRequestDTO;
import org.chanme.be.kiosk.domain.qusetion.Question;
import org.chanme.be.kiosk.domain.qusetion.QuestionItem;
import org.chanme.be.kiosk.domain.qusetion.questiondto.QuestionResponseDTO;
import org.chanme.be.kiosk.repository.ItemRepository;
import org.chanme.be.kiosk.repository.OptionGroupRepository;
import org.chanme.be.kiosk.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class KioskService {

    private final QuestionRepository questionRepo;
    private final ItemRepository itemRepo;
    private final OptionGroupRepository optionGroupRepo;

    public KioskService(QuestionRepository questionRepo , ItemRepository itemRepo, OptionGroupRepository optionGroupRepo) {
        this.questionRepo = questionRepo;
        this.itemRepo = itemRepo;
        this.optionGroupRepo = optionGroupRepo;
    }



    /**
     * 사용자의 주문과 정답을 비교하여 결과를 반환합니다. (리팩토링된 메인 메서드)
     */
    public OrderCheckResultDTO checkOrder(Long questionId, OrderRequestDTO orderReq) {

        // 1. 문제(정답) 조회 - 기존 Repository 메서드 그대로 사용
        Question question = questionRepo.find(questionId);
        if (question == null) {
            throw new NoSuchElementException("해당 문제를 찾을 수 없습니다. id=" + questionId);
        }

        List<OrderCheckResultDTO.MismatchDTO> mismatches = new ArrayList<>();

        // 2. 포장 및 결제 방식 비교 로직을 별도 메서드로 분리
        checkPackagingAndPayment(question, orderReq, mismatches);

        // 3. 주문 아이템과 정답 아이템 비교 로직을 별도 메서드로 분리
        compareOrderItems(question, orderReq, mismatches);

        boolean isCorrect = mismatches.isEmpty();
        return new OrderCheckResultDTO(isCorrect, mismatches);
    }

    public List<SimpleItemDTO> getSimpleItemsByCategory(Long categoryId) {
        List<SimpleItemDTO> items = itemRepo.findByCategory(categoryId)
                .stream()
                .map(SimpleItemDTO::from)
                .collect(Collectors.toList());

        if (items.isEmpty()) {
            throw new NoSuchElementException("해당 카테고리에 아이템이 없습니다. categoryId=" + categoryId);
        }
        return items;
    }


    /**
     * 아이템에 적용될 수 있는 모든 옵션(아이템, 카테고리)을 옵션 값과 함께 반환합니다.
     */
    public List<OptionGroupResponseDTO> getOptionsByItem(Long itemId) {
        // 1. 아이템 정보를 조회하여 카테고리 ID를 얻습니다.
        Item item = itemRepo.find(itemId);
        if (item == null) {
            throw new NoSuchElementException("해당 아이템을 찾을 수 없습니다. id=" + itemId);
        }
        Long categoryId = item.getCategory().getId();

        // 2. 새로운 리포지토리 메서드를 호출하여 모든 관련 옵션 그룹을 가져옵니다.
        List<OptionGroup> optionGroups = optionGroupRepo.findByItemAndCategoryWithOptions(itemId, categoryId);

        // 3. DTO로 변환하여 반환합니다.
        return optionGroups.stream()
                .map(OptionGroupResponseDTO::from)
                .collect(Collectors.toList());
    }


    /**
     * 주어진 KioskType에 해당하는 문제 중 하나를 랜덤으로 선택하여 반환합니다.
     */
    public QuestionResponseDTO getRandomQuestion(KioskType kioskType) {


        // 1. 해당 키오스크 타입의 모든 문제를 조회합니다.
        List<Question> questions = questionRepo.findByKioskType(kioskType);

        // 2. 만약 문제가 하나도 없다면 예외를 발생시킵니다.
        if (questions.isEmpty()) {
            throw new NoSuchElementException("해당 키오스크 타입에 대한 문제가 없습니다: " + kioskType);
        }

        // 3. 조회된 문제 리스트에서 랜덤으로 하나를 선택합니다.
        Random random = new Random();
        Question randomQuestion = questions.get(random.nextInt(questions.size()));

        // 4. 선택된 문제를 DTO로 변환하여 반환합니다.
        return QuestionResponseDTO.from(randomQuestion);
    }






    /**
     * 포장 및 결제 방식을 비교하고, 불일치 시 리스트에 추가합니다.
     */
    private void checkPackagingAndPayment(Question question, OrderRequestDTO orderReq, List<OrderCheckResultDTO.MismatchDTO> mismatches) {
        if (!question.getPackaging().equals(orderReq.getPackaging())) {
            mismatches.add(OrderCheckResultDTO.MismatchDTO.builder()
                    .type(MismatchType.PACKAGING)
                    .expected(question.getPackaging())
                    .actual(orderReq.getPackaging())
                    .message("포장 방식이 달라요. 정답은 '" + question.getPackaging().getDescription() + "' 입니다.")
                    .build());
        }
        if (!question.getPaymentMethod().equals(orderReq.getPaymentMethod())) {
            mismatches.add(OrderCheckResultDTO.MismatchDTO.builder()
                    .type(MismatchType.PAYMENT_METHOD)
                    .expected(question.getPaymentMethod())
                    .actual(orderReq.getPaymentMethod())
                    .message("결제 방식이 달라요. 정답은 '" + question.getPaymentMethod().getDescription() + "' 입니다.")
                    .build());
        }
    }




    /**
     * 주문된 아이템 목록과 정답 아이템 목록을 비교합니다.
     */
    private void compareOrderItems(Question question, OrderRequestDTO orderReq, List<OrderCheckResultDTO.MismatchDTO> mismatches) {
        // 정답 아이템을 Map으로 변환하여 쉽게 조회할 수 있도록 함
        Map<Long, QuestionItem> answerItemMap = question.getItems().stream()
                .collect(Collectors.toMap(qi -> qi.getItem().getId(), qi -> qi));

        // 사용자가 주문한 아이템을 순회하며 정답과 비교
        for (OrderItemDTO orderItem : orderReq.getItems()) {
            Long itemId = orderItem.getItemId();
            QuestionItem answerItem = answerItemMap.get(itemId);

            if (answerItem == null) {
                // 불필요한 아이템
                mismatches.add(OrderCheckResultDTO.MismatchDTO.builder()
                        .type(MismatchType.ITEM_UNNECESSARY)
                        .itemId(orderItem.getItemId())
                        .itemName(itemRepo.find(orderItem.getItemId()).getName()) // 아이템 이름을 조회해서 추가
                        .message("이 아이템은 주문할 필요가 없어요.")
                        .build());
                continue;
            }

            // 아이템별 수량 및 옵션 비교 로직을 별도 메서드로 분리
            checkItemDetails(orderItem, answerItem, mismatches);

            // 비교가 끝난 정답 아이템은 맵에서 제거
            answerItemMap.remove(itemId);
        }

        // 누락된 아이템
        for (QuestionItem missedItem : answerItemMap.values()) {
            mismatches.add(OrderCheckResultDTO.MismatchDTO.builder()
                    .type(MismatchType.ITEM_MISSING)
                    .itemId(missedItem.getItem().getId())
                    .itemName(missedItem.getItem().getName())
                    .message("'" + missedItem.getItem().getName() + "' 아이템을 주문하지 않으셨어요.")
                    .build());
        }
    }

    /**
     * 단일 아이템의 수량과 옵션을 비교합니다.
     */
    private void checkItemDetails(OrderItemDTO orderItem, QuestionItem answerItem, List<OrderCheckResultDTO.MismatchDTO> mismatches) {
        // 수량 비교
        if (orderItem.getQuantity() != answerItem.getQuantity()) {
            mismatches.add(OrderCheckResultDTO.MismatchDTO.builder()
                    .type(MismatchType.ITEM_QUANTITY)
                    .itemId(answerItem.getItem().getId())
                    .itemName(answerItem.getItem().getName())
                    .expected(answerItem.getQuantity())
                    .actual(orderItem.getQuantity())
                    .message("'" + answerItem.getItem().getName() + "'의 수량이 달라요.")
                    .build());
        }

        // 옵션 비교 로직을 별도 메서드로 분리
        checkItemOptions(orderItem, answerItem, mismatches);
    }

    /**
     * 아이템의 옵션 선택 사항을 비교합니다.
     */
    private void checkItemOptions(OrderItemDTO orderItem, QuestionItem answerItem, List<OrderCheckResultDTO.MismatchDTO> mismatches) {
        Long itemId = orderItem.getItemId();
        String itemName = answerItem.getItem().getName();

        // 1. 정답 옵션 정보를 다루기 쉽게 두 개의 Map으로 가공합니다.
        // Map<GroupId, Set<ValueId>>: 정답 옵션 그룹별 선택 ID 맵
        Map<Long, Set<Long>> answerOptionsByGroup = answerItem.getOptions().stream()
                .collect(Collectors.groupingBy(
                        qo -> qo.getGroup().getId(),
                        Collectors.mapping(qo -> qo.getValue().getId(), Collectors.toSet())
                ));

        // Map<GroupId, GroupName>: 메시지를 친절하게 만들기 위한 그룹 ID-이름 맵
        Map<Long, String> groupNames = answerItem.getOptions().stream()
                .collect(Collectors.toMap(
                        qo -> qo.getGroup().getId(),
                        qo -> qo.getGroup().getName(),
                        (name1, name2) -> name1 // 중복 키가 있을 경우 첫 번째 값 사용
                ));


        // 2. 사용자가 선택한 옵션을 순회하며 '선택이 틀린 경우'를 검사합니다.
        for (var selection : orderItem.getSelections()) {
            Long groupId = selection.getGroupId();
            Set<Long> expectedOptionIds = answerOptionsByGroup.getOrDefault(groupId, Collections.emptySet());
            Set<Long> actualOptionIds = new HashSet<>(selection.getSelectedValueIds());

            if (!expectedOptionIds.equals(actualOptionIds)) {
                mismatches.add(OrderCheckResultDTO.MismatchDTO.builder()
                        .type(MismatchType.OPTION_SELECTION)
                        .itemId(itemId)
                        .itemName(itemName)
                        .expected(expectedOptionIds) // 정답 옵션 ID 목록
                        .actual(actualOptionIds)     // 사용자 선택 ID 목록
                        .message(String.format("'%s'의 '%s' 옵션 선택이 잘못되었어요.", itemName, groupNames.getOrDefault(groupId, "알 수 없는")))
                        .build());
            }

            // 검사가 끝난 그룹은 정답 맵에서 제거합니다.
            answerOptionsByGroup.remove(groupId);
        }

        // 3. 정답 맵에 남아있는 그룹은 '사용자가 누락한 옵션 그룹'입니다.
        for (Long missingGroupId : answerOptionsByGroup.keySet()) {
            mismatches.add(OrderCheckResultDTO.MismatchDTO.builder()
                    .type(MismatchType.OPTION_GROUP_MISSING)
                    .itemId(itemId)
                    .itemName(itemName)
                    .expected(answerOptionsByGroup.get(missingGroupId)) // 누락된 그룹의 정답
                    .actual(null) // 사용자는 선택하지 않았으므로 null
                    .message(String.format("'%s' 에서 '%s' 옵션을 선택하지 않으셨어요.", itemName, groupNames.get(missingGroupId)))
                    .build());
        }
    }


}
