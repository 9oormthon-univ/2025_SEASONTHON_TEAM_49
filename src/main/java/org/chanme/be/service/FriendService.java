package org.chanme.be.service;

import java.util.List;

import org.chanme.be.domain.friend.Friendship;
import org.chanme.be.domain.friend.FriendshipRepository;
import org.chanme.be.domain.user.User;
import org.chanme.be.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FriendService {

    private final UserRepository userRepo;
    private final FriendshipRepository friendRepo;

    public FriendService(UserRepository userRepo, FriendshipRepository friendRepo) {
        this.userRepo = userRepo;
        this.friendRepo = friendRepo;
    }

    /** 회원코드로 친구 추가 (양방향 저장) */
    @Transactional
    public void addFriendByMemberCode(String myMemberCode, String targetMemberCode) {
        if (myMemberCode == null || targetMemberCode == null || targetMemberCode.isBlank()) {
            throw new IllegalArgumentException("회원코드를 입력해 주세요.");
        }
        if (myMemberCode.equals(targetMemberCode)) {
            throw new IllegalArgumentException("본인은 친구로 추가할 수 없어요.");
        }

        // 본인 조회
        User me = userRepo.findByMemberCode(myMemberCode)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 만료되었거나 잘못됐어요. 다시 로그인해 주세요."));

        // 친구조회
        User friend = userRepo.findByMemberCode(targetMemberCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원코드를 가진 사용자가 없어요."));

        // A -> B
        if (!friendRepo.existsByUserIdAndFriendId(me.getId(), friend.getId())) {
            friendRepo.save(Friendship.builder().userId(me.getId()).friendId(friend.getId()).build());
        }
        // B -> A (양방향)
        if (!friendRepo.existsByUserIdAndFriendId(friend.getId(), me.getId())) {
            friendRepo.save(Friendship.builder().userId(friend.getId()).friendId(me.getId()).build());
        }
    }

    public List<Friendship> getMyFriendships(Long myUserId) {
        return friendRepo.findByUserId(myUserId);
    }
}
