package me.minho.reservation.service;

import me.minho.reservation.domain.Member;
import me.minho.reservation.domain.Shop;
import me.minho.reservation.repository.MemberRepository;
import me.minho.reservation.repository.ShopRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final ShopRepository shopRepository;

    public MemberService(MemberRepository memberRepository, ShopRepository shopRepository) {
        this.memberRepository = memberRepository;
        this.shopRepository = shopRepository;
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    // Member 의 참조로 변수로 Shop 을 넣을 수도 있었는데 이렇게 따로 메서드를 분리해서 회원가입 코드를 작성 한 이유는
    // 1. 당장 Member 의 shop 매개변수가 비즈니스 로직에서 안 쓰임
    // 2. Member <-> Shop 간 양방향 참조가 일어남
    // 3. Member 에 Shop 이라는 nullable 변수를 추가해야함 (null 체크 등의 귀찮음..)
    // 4. saveAdmin 이란 메서드를 분리하면 admin 을 추가할 때 가 Shop 이라는 객체가 꼭 필요하다는 것을 메서드 시그니처에서 파악가능
    public Member saveAdminMember(Member member, Shop shop) {
        memberRepository.save(member);
        shopRepository.save(shop);
        return member;
    }
}
