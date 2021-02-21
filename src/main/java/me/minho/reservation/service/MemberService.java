package me.minho.reservation.service;

import me.minho.reservation.domain.Member;
import me.minho.reservation.domain.Shop;
import me.minho.reservation.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final ShopService shopService;

    public MemberService(MemberRepository memberRepository, ShopService shopService) {
        this.memberRepository = memberRepository;
        this.shopService = shopService;
    }

    public Member join(Member member) {
        return memberRepository.save(member);
    }

    public Member joinAdminMember(Member member, Shop shop) {
        join(member);
        shopService.addShop(shop);
        return member;
    }

    @Transactional(readOnly = true)
    public boolean login(String email, String password, HttpSession httpSession) {
        Member member = memberRepository.findByEmail(email).orElseThrow(IllegalStateException::new);
        return member.login(password, httpSession);
    }
}
