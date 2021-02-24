package me.minho.reservation.member.service;

import lombok.RequiredArgsConstructor;
import me.minho.reservation.member.domain.Member;
import me.minho.reservation.member.domain.MemberRepository;
import me.minho.reservation.shop.service.ShopService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final ShopService shopService;
    private final MemberRepository memberRepository;

    @Transactional
    public long signUp(Member member) {
        // 이메일 중복 검사
        if (memberRepository.findByEmail(member.getEmail()) != null) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다");
        }

        // 회원 생성
        memberRepository.save(member);

        // 관리자면 샵도 함께 생성
        if (member.isAdmin()) {
            shopService.save(member.getShop());
        }

        return member.getId();
    }
}
