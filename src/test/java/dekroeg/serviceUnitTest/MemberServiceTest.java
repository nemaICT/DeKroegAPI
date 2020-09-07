package dekroeg.serviceUnitTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import dekroeg.domain.Member;
import dekroeg.exception.ResourceNotFoundException;
import dekroeg.repository.MemberRepository;
import dekroeg.service.MemberService;
import dekroeg.util.MemberCreator;
import dekroeg.util.Utils;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class) // see https://www.youtube.com/watch?v=pTgD8cbkVys
class MemberServiceTest {

    // test the MemberControllerTest class using Mockito
    //Use the @InjectMocks annotation to test the target class in example the MemberController is the target class
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepositoryMock;

    @Mock
    private Utils utilsMock;

    @BeforeEach
    public void setup(){

        // this is the setup to run the @GetMapping --> listAll() from the MemberController
        PageImpl<Member> memberPage = new PageImpl<>(List.of(MemberCreator.createValidMember()));
        BDDMockito.when(memberRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(memberPage);

        // this is the setup to run the @GetMapping --> findById() from the MemberController
        BDDMockito.when(memberRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(MemberCreator.createValidMember()));

        // this is the setup to run the @GetMapping --> findByFirstname() from the MemberController
        BDDMockito.when(memberRepositoryMock.findByFirstname(ArgumentMatchers.anyString()))
                .thenReturn(List.of(MemberCreator.createValidMember()));

        // this is the setup to run the @PostMapping --> saveMember() from the MemberController
        BDDMockito.when(memberRepositoryMock.save(MemberCreator.createMember()))
                .thenReturn(MemberCreator.createValidMember());

        // this is the setup to run the @DeleteMapping --> delete() from the MemberController
        BDDMockito.doNothing().when(memberRepositoryMock).delete(ArgumentMatchers.any(Member.class));

        // this is the setup to run the @PutMapping --> updateMember() from the MemberController
        BDDMockito.when(memberRepositoryMock.save(MemberCreator.createValidMember()))
                .thenReturn(MemberCreator.createValidUpdateMember());

        // this is the setup to run the @GetMapping --> findById() from the MemberController
        BDDMockito.when(
                utilsMock.findMemberOrThrowNotFound(ArgumentMatchers.anyLong(), ArgumentMatchers.any(MemberRepository.class)))
                .thenReturn(MemberCreator.createValidMember());
    }


    // for full follow alone see: https://www.youtube.com/watch?v=EjD__EJRPe0
    @Test
    @DisplayName("listAll returns a pageable list of members when succesful")
    public void listAll_ReturnsListOfMembersInsidePageObject_WhenSuccesful(){

        String expectedName = MemberCreator.createMember().getFirstname();

        Page<Member> memberPage = memberService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(memberPage).isNotNull();

        Assertions.assertThat(memberPage.toList()).isNotEmpty();

        Assertions.assertThat(memberPage.toList().get(0).getFirstname()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns a member when succesful")
    public void findById_ReturnsAMember_WhenSuccesful(){

        Long expectedId = MemberCreator.createValidMember().getId();

        Member member = memberService.findById(1l);

        Assertions.assertThat(member).isNotNull();

        Assertions.assertThat(member.getId()).isNotNull();

        Assertions.assertThat(member.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns a pageable list of members when succesful")
    public void findByName_ReturnsAMember_WhenSuccesful(){


        String expectedFirstname = MemberCreator.createMember().getFirstname();

        List<Member> membersList = memberService.findByFirstname("Iara");

        Assertions.assertThat(membersList).isNotNull();

        Assertions.assertThat(membersList).isNotEmpty();

        Assertions.assertThat(membersList.get(0).getFirstname()).isEqualTo(expectedFirstname);
    }

    @Test
    @DisplayName("Create a member when succesful")
    public void save_CreatesMember_WhenSuccesful(){

        Long expectedId = MemberCreator.createValidMember().getId();

        Member memberToBeSaved = MemberCreator.createMember();

        Member member = memberService.saveMember(memberToBeSaved);

        Assertions.assertThat(member).isNotNull();

        Assertions.assertThat(member.getId()).isNotNull();

        Assertions.assertThat(member.getId()).isEqualTo(expectedId);
    }

    // because the deleteMember() of the MemberService returns a void, the only thing we need to do is
    // assert that the code doesNotThrowAnyException();
    @Test
    @DisplayName("Delete removes a member when succesful")
    public void delete_RemovesMember_WhenSuccesful(){

       Assertions.assertThatCode(() -> memberService.deleteMember(1l))
               .doesNotThrowAnyException();
     }

    @Test
    @DisplayName("Delete throws ResourceNotFoundException when the member doesn't exist")
    public void delete_ThrowsResourceNotFoundException_WhenMemberDoNotExist(){

        BDDMockito.when(
                utilsMock.findMemberOrThrowNotFound(ArgumentMatchers.anyLong(), ArgumentMatchers.any(MemberRepository.class)))
                .thenThrow(new ResourceNotFoundException("member not found"));

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> memberService.deleteMember(1l));

    }

    @Test
    @DisplayName("Update save a member when succesful")
    public void update_SavepdatedMember_WhenSuccesful(){

        Member validUpdatedMember = MemberCreator.createValidUpdateMember();

        String expectedName = validUpdatedMember.getFirstname();

        Member member = memberService.saveMember(MemberCreator.createValidMember());

        Assertions.assertThat(member).isNotNull();

        Assertions.assertThat(member.getId()).isNotNull();

        Assertions.assertThat(member.getFirstname()).isEqualTo(expectedName);
    }

}
