package com.mysite.sbb.member.controller;

import com.mysite.sbb.member.dto.MemberDto;
import com.mysite.sbb.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인하세요.");
        return "member/login";
    }

    @GetMapping("/signup")
    public String signup(MemberDto memberDto) {
        return "member/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberDto memberDto,
                         BindingResult bindingResult,
                         Model model) {

        log.info("===== MemberDto : {}", memberDto);

        if(bindingResult.hasErrors()) {
            return "member/signup";
        }

        if(!memberDto.getPassword1().equals(memberDto.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordIncorrect",
                    "입력된 두개의 패스워드가 일치하지 않습니다.");
            return "member/signup";
        }

        try{
            memberService.createMember(memberDto);
        } catch (DataIntegrityViolationException e){
            e.printStackTrace();
            model.addAttribute("errorMsg", "이미 등록된 사용자 입니다.");
            return "member/signup";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg", e.getMessage());
            return "member/signup";
        }

        return "redirect:/";
    }
}
