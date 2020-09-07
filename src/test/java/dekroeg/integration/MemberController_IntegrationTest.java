package dekroeg.integration;

import dekroeg.util.MemberCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import dekroeg.domain.Member;
import dekroeg.repository.MemberRepository;
import dekroeg.wrapper.PageableResponse;

import java.util.List;
import java.util.Optional;

//@SpringBootTest --> using this annotation the database must be running in order to execute the tests otherwise the tests will fail
// defining the WebEnvironment as RANDOM_PORT we make sure that this everytime we run this test the server will use a random port
// and this test will not crash
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberController_IntegrationTest { // for further details about this classe see: https://www.youtube.com/watch?v=EjD__EJRPe0


    /* TestRestTemplate helps us to send http request in our integration tests. To do that we need all application context
       should be running. Also Spring run a local server in a random port @LocalServerPort. So just need to create the
       request in integration tests and send it like a clients of your servers. TestRestTemplate have all necessary
       methods to send the request to server with a convenient way similar to RestTemplate.*/
    @Autowired
    @Qualifier(value = "testResttemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;
    @Autowired
    @Qualifier(value = "testResttemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;


//    @LocalServerPort // using this annotation allow us to find out which port we will be using
//    private int port;


    @MockBean
    private MemberRepository memberRepositoryMock;
    /*We use the @MockBean to add mock objects to the Spring application context.
    The mock will replace any existing bean of the same type in the application context.
    If no bean of the same type is defined, a new one will be added.
    This annotation is useful in integration tests where a particular bean – for example,
    an external service – needs to be mocked.*/


    @Lazy
    @TestConfiguration
    static class Config{

        @Bean(name="testResttemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port){
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("iara", "iara");
            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name="testResttemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRowAdminCreator(@Value("${local.server.port}") int port){
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("nema", "nema");
            return new TestRestTemplate(restTemplateBuilder);
        }
    }

    @BeforeEach
    public void setup(){

//        // this is the setup to run the @GetMapping --> listAll() from the MemberController
//        PageImpl<Member> memberPage = new PageImpl<>(List.of(MemberCreator.createValidMember()));
//        BDDMockito.when(memberRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
//                .thenReturn(memberPage);

        Page<Member> memberPage2 = new PageImpl<>(List.of(MemberCreator.createValidMember()));
        BDDMockito.when(memberRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(memberPage2);

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
    }

    // for full follow alone see: https://www.youtube.com/watch?v=EjD__EJRPe0
    @Test
    @DisplayName("listAll returns a pageable list of members when succesful")
    public void listAll_ReturnsListOfMembersInsidePageObject_WhenSuccesful(){

        String expectedName = MemberCreator.createMember().getFirstname();

        /*Execute call to the server to get a page with members*/
        //@formatter:off
        Page<Member> memberPage = testRestTemplateRoleUser.exchange("/members", HttpMethod.GET,
                null, new ParameterizedTypeReference<PageableResponse<Member>>() {}).getBody();
        //@formatter:on

        Assertions.assertThat(memberPage).isNotNull();

        Assertions.assertThat(memberPage.toList()).isNotEmpty();

        Assertions.assertThat(memberPage.toList().get(0).getFirstname()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns a member when succesful")
    public void findById_ReturnsAMember_WhenSuccesful(){

        Long expectedId = MemberCreator.createValidMember().getId();

        Member member = testRestTemplateRoleUser.getForObject("/members/1", Member.class);

        Assertions.assertThat(member).isNotNull();

        Assertions.assertThat(member.getId()).isNotNull();

        Assertions.assertThat(member.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns a list of members when succesful")
    public void findByName_ReturnsListOfMembers_WhenSuccesful(){


        String expectedFirstname = MemberCreator.createMember().getFirstname();

        //@formatter:off
        List<Member> membersList = testRestTemplateRoleUser.exchange("/members/find?firstname='Iara'",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Member>>() {}).getBody();
        //@formatter:on
        Assertions.assertThat(membersList).isNotNull();

        Assertions.assertThat(membersList).isNotEmpty();

        Assertions.assertThat(membersList.get(0).getFirstname()).isEqualTo(expectedFirstname);
    }

    @Test
    @DisplayName("Create a member when succesful")
    public void save_CreatesMember_WhenSuccesful(){

        Long expectedId = MemberCreator.createValidMember().getId();

        Member memberToBeSaved = MemberCreator.createMember();

        Member member = testRestTemplateRoleUser.exchange("/members", HttpMethod.POST,
                createJsonHttpEntity(memberToBeSaved), Member.class).getBody();

        Assertions.assertThat(member).isNotNull();

        Assertions.assertThat(member.getId()).isNotNull();

        Assertions.assertThat(member.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("Delete removes a member when succesful")
    public void delete_RemovesMember_WhenSuccesful(){

     ResponseEntity<Void> responseEntity = testRestTemplateRoleAdmin.exchange("/members/admin/1", HttpMethod.DELETE,
             null, Void.class);

        Assertions.assertThat(responseEntity).isNotNull();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Assertions.assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("Delete returns forbidden when user does not have role admin")
    public void delete_Returns403_WhenUserIsNotAdmin(){

        ResponseEntity<Void> responseEntity = testRestTemplateRoleUser.exchange("/members/admin/1", HttpMethod.DELETE,
                null, Void.class);

        Assertions.assertThat(responseEntity).isNotNull();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    @DisplayName("Update save a member when succesful")
    public void update_SavepdatedMember_WhenSuccesful(){

        Member validMember = MemberCreator.createValidMember();
        ResponseEntity<Void> responseEntity = testRestTemplateRoleUser.exchange("/members", HttpMethod.PUT,
                createJsonHttpEntity(validMember), Void.class);

        Assertions.assertThat(responseEntity).isNotNull();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Assertions.assertThat(responseEntity.getBody()).isNull();

    }


    private HttpEntity<Member> createJsonHttpEntity(Member member){
        return new HttpEntity<>(member, createJsonHeaders());
         /* An HTTP entity is the majority of an HTTP request or response, consisting of some of the headers and the body,
       if present. It seems to be the entire request or response without the request or status line (although only certain
       header fields are considered part of the entity).
       ex:  POST /foo HTTP/1.1          # Not part of the entity.
            Content-Type: text/plain    # ┬ The entity is from this line down...
            Content-Length: 1234        # │
                                        # │
            Hello, World! ...           # ┘*/
    }


    private static HttpHeaders createJsonHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
