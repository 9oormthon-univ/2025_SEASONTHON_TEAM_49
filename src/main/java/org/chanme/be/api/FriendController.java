// src/main/java/org/chanme/be/api/FriendController.java
package org.chanme.be.api;

import org.chanme.be.api.dto.FriendDtos;
import org.chanme.be.api.dto.FriendDtos.AddFriendReq;
import org.chanme.be.api.dto.FriendDtos.FriendItem;
import org.chanme.be.api.dto.FriendDtos.HomeSummaryRes;
import org.chanme.be.api.dto.FriendDtos.Me;
import org.chanme.be.auth.CookieAuthInterceptor;
import org.chanme.be.domain.friend.Friendship;
import org.chanme.be.domain.user.User;
import org.chanme.be.domain.user.UserRepository;
import org.chanme.be.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "친구", description = "친구 추가 및 홈 화면 요약 API")
public class FriendController {

    private final FriendService friendService;
    private final UserRepository userRepo;

    public FriendController(FriendService friendService, UserRepository userRepo) {
        this.friendService = friendService;
        this.userRepo = userRepo;
    }

    @Operation(
        summary = "친구 추가 (양방향)",
        description = """
                      회원코드를 입력해 친구를 추가합니다.
                      성공 시 A→B, B→A 두 방향의 관계가 모두 생성됩니다.
                      """
    )
    @PostMapping("/friends/add")
    public ResponseEntity<?> addFriend(@RequestBody AddFriendReq req, HttpServletRequest httpReq) {

        User me = CookieAuthInterceptor.currentUser(httpReq);
        if (me == null) return ResponseEntity.status(401).body("로그인이 필요해요");

        try {
            friendService.addFriendByMemberCode(me.getMemberCode(), req.memberCode());
            return ResponseEntity.ok("친구를 추가했어요");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
        summary = "홈 화면 요약",
        description = "내 총점(hapScore)과 친구들의 최신 총점을 반환합니다. 친구 리스트는 총점 내림차순 정렬."
    )
    @GetMapping("/home/summary")
    public ResponseEntity<?> homeSummary(HttpServletRequest httpReq) {

        User me = CookieAuthInterceptor.currentUser(httpReq);
        if (me == null) return ResponseEntity.status(401).body("로그인이 필요해요");

        List<Friendship> fs = friendService.getMyFriendships(me.getId());
        List<Long> friendIds = fs.stream().map(Friendship::getFriendId).toList();

        var friends = friendIds.isEmpty() ? List.<User>of() : userRepo.findByIdIn(friendIds);

        var friendItems = friends.stream()
                .map(u -> new FriendItem(u.getId(), u.getName(), u.getMemberCode(), u.getHapScore() == null ? 0 : u.getHapScore()
                ))
                .sorted(Comparator.comparing(FriendItem::hapScore, Comparator.nullsFirst(Comparator.naturalOrder())).reversed())
                .toList();

        var meDto = new Me(me.getId(), me.getName(), me.getMemberCode(), me.getHapScore() == null ? 0 : me.getHapScore());

        return ResponseEntity.ok(new HomeSummaryRes(meDto, friendItems));
    }
}