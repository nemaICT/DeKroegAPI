package dekroeg.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import dekroeg.domain.Member;
import dekroeg.service.MemberService;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("members")
@Slf4j
@RequiredArgsConstructor
/*With Lombok's(@RequiredArgsConstructor), it's possible to generate a constructor for either all class's fields (with @AllArgsConstructor)
or all final class's fields (with @RequiredArgsConstructor). Moreover, if you still need an empty constructor,
you can append an additional @NoArgsConstructor annotation.*/
public class MemberController {

    private final MemberService memberService;


    @GetMapping
    public ResponseEntity<Page<Member>> listAll(Pageable pageable){
      return ResponseEntity.ok(memberService.listAll(pageable));
    }
    @GetMapping(path = "/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Member> findById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        log.info("User details {}", userDetails);
        return ResponseEntity.ok(memberService.findById(id));
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Member>> findByName(@RequestParam(value = "firstname") String firstname){
        return ResponseEntity.ok(memberService.findByFirstname(firstname));
    }

    @PostMapping
    public ResponseEntity<Member> saveMember(@RequestBody @Valid Member member){
        return ResponseEntity.ok(memberService.saveMember(member));
    }

    @DeleteMapping(path = "/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        memberService.deleteMember(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> updateMember(@RequestBody Member member){
        memberService.updateMember(member);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
