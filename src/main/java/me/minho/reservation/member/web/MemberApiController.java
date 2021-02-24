package me.minho.reservation.member.web;

import lombok.RequiredArgsConstructor;
import me.minho.reservation.member.service.MemberService;
import me.minho.reservation.member.web.dto.MemberSaveRequest;
import me.minho.reservation.util.RestResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/members")
    public RestResponseData<Long> signUp(@RequestBody MemberSaveRequest memberSaveRequest) {
        try {
            return new RestResponseData(HttpStatus.OK, memberService.signUp(memberSaveRequest.toMember()), "회원가입 성공!");
        } catch (IllegalArgumentException e) {
            return new RestResponseData(HttpStatus.OK, e.getMessage());
        }
    }
}
