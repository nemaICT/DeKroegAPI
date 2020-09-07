package dekroeg.client;

import dekroeg.wrapper.PageableResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import dekroeg.domain.Member;

@Slf4j
public class SpringClient {
    public static void main(String[] args) {

        System.out.println(new BCryptPasswordEncoder().encode("nema"));

        //@formatter:off
        ResponseEntity<PageableResponse<Member>> exchangeMemberList =  new RestTemplate()
                .exchange("http://localhost:8080/members?sort=firstname,desc", HttpMethod.GET,
                null, new ParameterizedTypeReference<PageableResponse<Member>>(){});
        //@formatter:on


        // Here we have the possibility of returning the exchangeMemberList, where the content information will also be returned like a header:
        //[Content-Type:"application/json", Transfer-Encoding:"chunked", Date:"Fri, 21 Aug 2020 08:47:53 GMT", Keep-Alive:"timeout=60", Connection:"keep-alive"]>
        log.info("list Of Members with header {}", exchangeMemberList);


        // creating another member with the builder
        Member newMember = Member.builder().firstname("Rafael")
                                           .lastname("Lopes")
                                           .build();

        // saving it to the database using the "RestTemplate.exchange" --> method
       // the .exchange() below first needs a "url', a "Http request", a Httpentity having a newmember and a optional JsonHeader as parameter, a object.class and finally the getBody
        Member exchangeSaved = new RestTemplate()
                               .exchange("http://localhost:8080/members",
                                              HttpMethod.POST,
                                              new HttpEntity<>(newMember, createJsonHeaders()), Member.class)
                                              .getBody();

        log.info("Saved Member id: {}", exchangeSaved.getId());



        // update members data
        exchangeSaved.setFirstname("Lina");
        ResponseEntity<Void> exchangeUpdated = new RestTemplate()
                                              .exchange("http://localhost:8080/members",
                                              HttpMethod.PUT, new HttpEntity<>(exchangeSaved, createJsonHeaders()), Void.class);

        log.info("Updated Member status: {}", exchangeUpdated.getStatusCode());


        // delete member
        ResponseEntity<Void> exchangeDelete = new RestTemplate()
                                             .exchange("http://localhost:8080/members/{id}",
                                              HttpMethod.DELETE, null, Void.class, exchangeSaved.getId());
        log.info("Deleted Member status: {}", exchangeDelete.getStatusCode());

    }

    // creating Json headers --> !!! its important to place this method inside of the new HttpEntity<>(newMember, createJsonHeaders()) in the .exchange Method
    // at line 76
    private static HttpHeaders createJsonHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private static void testGetWithRestTemplate() {
        ResponseEntity<Member> memberResponseEntity = new RestTemplate()
                //giving a parameter in the url using "{id}" and place the id value after the Member.class, 1
                .getForEntity("http://localhost:8080/members/{id}", Member.class, 1);

        log.info("Response Entity {}", memberResponseEntity);

        log.info("Response Data {}", memberResponseEntity.getBody());

        Member member = new RestTemplate()
                        .getForObject("http://localhost:8080/members/13", Member.class);

        log.info("Member {}", member);
    }
}
