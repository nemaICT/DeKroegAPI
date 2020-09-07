package dekroeg.controllerUnitTest;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import dekroeg.controller.MemberController;
import dekroeg.domain.Member;
import dekroeg.service.MemberService;
import dekroeg.util.MemberCreator;

import java.util.List;

//@SpringBootTest --> using this annotation the database must be running in order to execute the tests
// otherwise the tests will fail

// using this annotation we will be allowed to execute all the  tests without the need for the services be running
@ExtendWith(SpringExtension.class)
class MemberControllerTest {

    // for the MemmberControllerTest class we will use Mockito
    @InjectMocks //Use the @InjectMocks annotation to test the target class, in example the MemberController is the target class
    private MemberController memberControllerMock;
    @Mock // use the @Mock annotation for the classes that will support the target class as the MemberService.class does for the MemberController
    private MemberService memberServiceMock;


    @BeforeEach
    public void setup(){

        // this is the setup to run the @GetMapping --> listAll() from the MemberController
       PageImpl<Member> memberPage = new PageImpl<>(List.of(MemberCreator.createValidMember()));
        BDDMockito.when(memberServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(memberPage);

        // this is the setup to run the @GetMapping --> findById() from the MemberController
        BDDMockito.when(memberServiceMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(MemberCreator.createValidMember());

        // this is the setup to run the @GetMapping --> findByFirstname() from the MemberController
        BDDMockito.when(memberServiceMock.findByFirstname(ArgumentMatchers.anyString()))
                .thenReturn(List.of(MemberCreator.createValidMember()));

        // this is the setup to run the @PostMapping --> saveMember() from the MemberController
        BDDMockito.when(memberServiceMock.saveMember(MemberCreator.createMember()))
                .thenReturn(MemberCreator.createValidMember());

        // this is the setup to run the @DeleteMapping --> delete() from the MemberController
        BDDMockito.doNothing().when(memberServiceMock).deleteMember(ArgumentMatchers.anyLong());

        // this is the setup to run the @PutMapping --> updateMember() from the MemberController
        BDDMockito.when(memberServiceMock.saveMember(MemberCreator.createValidMember()))
                .thenReturn(MemberCreator.createValidUpdateMember());
    }


    @Test
    @DisplayName("listAll returns a pageable list of members when succesful")
    public void listAll_ReturnsListOfMembersInsidePageObject_WhenSuccesful(){

        String expectedName = MemberCreator.createMember().getFirstname();

        Page<Member> memberPage = memberControllerMock.listAll(null).getBody();

        Assertions.assertThat(memberPage).isNotNull();

        Assertions.assertThat(memberPage.toList()).isNotEmpty();

        Assertions.assertThat(memberPage.toList().get(0).getFirstname()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns a member when succesful")
    public void findById_ReturnsAMember_WhenSuccesful(){

        Long expectedId = MemberCreator.createValidMember().getId();

        Member member = memberControllerMock.findById(1l, null).getBody();

        Assertions.assertThat(member).isNotNull();

        Assertions.assertThat(member.getId()).isNotNull();

        Assertions.assertThat(member.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns a list of members when succesful")
    public void findByName_ReturnsListOfMembers_WhenSuccesful(){


        String expectedFirstname = MemberCreator.createMember().getFirstname();

        List<Member> membersList = memberControllerMock.findByName("Iara").getBody();

        Assertions.assertThat(membersList).isNotNull();

        Assertions.assertThat(membersList).isNotEmpty();

        Assertions.assertThat(membersList.get(0).getFirstname()).isEqualTo(expectedFirstname);
    }

    @Test
    @DisplayName("Create a member when succesful")
    public void save_CreatesMember_WhenSuccesful(){

        Long expectedId = MemberCreator.createValidMember().getId();

        Member memberToBeSaved = MemberCreator.createMember();

        Member member = memberControllerMock.saveMember(memberToBeSaved).getBody();

        Assertions.assertThat(member).isNotNull();

        Assertions.assertThat(member.getId()).isNotNull();

        Assertions.assertThat(member.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("Delete removes a member when succesful")
    public void delete_RemovesMember_WhenSuccesful(){

     ResponseEntity<Void> responseEntity = memberControllerMock.delete(1l);

        Assertions.assertThat(responseEntity).isNotNull();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Assertions.assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("Update save a member when succesful")
    public void update_SavepdatedMember_WhenSuccesful(){

        ResponseEntity<Void> responseEntity = memberControllerMock.updateMember(MemberCreator.createValidMember());

        Assertions.assertThat(responseEntity).isNotNull();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Assertions.assertThat(responseEntity.getBody()).isNull();

    }
}
