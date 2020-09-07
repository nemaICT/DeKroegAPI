package dekroeg.util;

import dekroeg.domain.Member;

public class MemberCreator {

    // as we are going to need a Member for this test let's create a method that returns a Member
    public static Member createMember(){
        return Member.builder()
                .firstname("Nema")
                .lastname("Lopes")
                .build();
    }


    // as we are going to need a Member for this test let's create a method that returns a Member
    public static Member createValidMember(){
        return Member.builder()
                .id(1l)
                .firstname("Nema")
                .lastname("Lopes")
                .build();
    }

    // as we are going to need a Member for this test let's create a method that returns a Member
    public static Member createValidUpdateMember(){
        return Member.builder()
                .id(1l)
                .firstname("Nema")
                .lastname("Lopes")
                .build();
    }
}
