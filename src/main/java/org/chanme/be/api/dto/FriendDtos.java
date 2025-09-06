package org.chanme.be.api.dto;

import java.util.List;

public class FriendDtos {

    // 친구 추가 요청
    public record AddFriendReq(String memberCode) {}

    // 홈 요약 응답
    public record HomeSummaryRes(Me me, List<FriendItem> friends) {}

    public record Me(Long id, String name, String memberCode, Integer hapScore) {}

    public record FriendItem(Long id, String name, String memberCode, Integer hapScore) {}
}