package dekroeg.service;

import dekroeg.repository.MemberRepository;
import dekroeg.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import dekroeg.domain.Member;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberService {

    private final Utils utils;
    private final MemberRepository memberRepository;


    // RestTemplate Pageable
    public Page<Member> listAll(Pageable pageable){
        return memberRepository.findAll(pageable);
    }

    public List<Member> findByFirstname(String firstname){return memberRepository.findByFirstname(firstname); }

    public Member findById(Long id){
      return utils.findMemberOrThrowNotFound(id, memberRepository);
    }

    @Transactional
    public Member saveMember(Member member){
        return memberRepository.save(member);
    }

    public void deleteMember(Long id){
      memberRepository.delete(utils.findMemberOrThrowNotFound(id, memberRepository));
    }

    public void updateMember(Member member){
        memberRepository.save(member);
    }
}
