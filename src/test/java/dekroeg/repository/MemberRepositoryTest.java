package dekroeg.repository;

import dekroeg.util.MemberCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import dekroeg.domain.Member;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@DataJpaTest // this annotation tell Spring that we are testing the Jpa repository
@DisplayName("Member Repository Test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Save persist Member when succesful")
    public void save_PersitMember_WhenSusccesful(){
        // create a new Member
        Member member = MemberCreator.createMember();

        // insert member into database
        Member savedMember = this.memberRepository.save(member);

        Assertions.assertThat(savedMember.getId()).isNotNull();
        Assertions.assertThat(savedMember.getFirstname()).isNotNull();
        Assertions.assertThat(savedMember.getLastname()).isNotNull();
        Assertions.assertThat(savedMember.getFirstname()).isEqualTo(savedMember.getFirstname());
        Assertions.assertThat(savedMember.getLastname()).isEqualTo(savedMember.getLastname());

    }

    @Test
    @DisplayName("Update Member when succesful")
    public void updatedMember_WhenSusccesful(){
        // create a new Member
        Member member = MemberCreator.createMember();

        // insert member into database and assign its value to a new Member
        Member savedMember = this.memberRepository.save(member);
        savedMember.setFirstname("Noah");

        Member updatedMember = this.memberRepository.save(savedMember);

        Assertions.assertThat(savedMember.getId()).isNotNull();
        Assertions.assertThat(savedMember.getFirstname()).isNotNull();
        Assertions.assertThat(savedMember.getLastname()).isNotNull();
        Assertions.assertThat(savedMember.getFirstname()).isEqualTo(updatedMember.getFirstname());
        Assertions.assertThat(savedMember.getLastname()).isEqualTo(updatedMember.getLastname());

    }


    @Test
    @DisplayName("Delete Member when succesful")
    public void delete_Member_WhenSusccesful(){
        // create a new Member
        Member member = MemberCreator.createMember();

        // insert member into database and assign its value to a new Member
        Member savedMember = this.memberRepository.save(member);

        this.memberRepository.delete(member);

        Optional<Member> deletedMember = this.memberRepository.findById(savedMember.getId());

        Assertions.assertThat(deletedMember.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Find by name returns member when succesful")
    public void findByName_ReturnsMember_WhenSusccesful(){

        // create a new Member
        Member member = MemberCreator.createMember();

        // save member into database
        Member savedMember =  this.memberRepository.save(member);


        // get firstname from the savedMember and place into a variable to be
        String firstname = savedMember.getFirstname();


        // mock a database using a list in place of
        List<Member> memberList = this.memberRepository.findByFirstname(firstname);

        Assertions.assertThat(memberList).isNotEmpty();

        Assertions.assertThat(memberList).contains(savedMember);
    }

    @Test
    @DisplayName("Find by name returns empty when no member is found")
    public void findByName_ReturnsEmptyList_WhenNoMemberIsFound(){

        // get firstname from the savedMember and place into a variable to be
        String firstname = "fake-name";

        // mock a database using a list in place of
        List<Member> memberList = this.memberRepository.findByFirstname(firstname);

        Assertions.assertThat(memberList).isEmpty();


    }

    // testing for throwing exceptions.
    // two ways are used to achieve this job
    @Test
    @DisplayName("Throw ConstraintViolationException when firstname is empty")
    public void throw_ConstraintViolationException_WhenFristnameIsEmpty(){

       //create a member object with default constructor
        Member member = new Member();

//        // first way
//        Assertions.assertThatThrownBy(() -> memberRepository.save(member))
//                .isInstanceOf(ConstraintViolationException.class);

        // second way
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> memberRepository.save(member))
                .withMessageContaining("Firstname can't be empty");


    }
}
