package by.youngliqui.EShopProject.repositories;

import by.youngliqui.EShopProject.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private UserRepository userRepository;

    @Test
    void checkFindByName() {
        User user = User.builder()
                .name("test user")
                .password("pass")
                .email("test@gmail.com")
                .build();

        testEntityManager.persist(user);

        User actualUser = userRepository.findFirstByName(user.getName());

        assertThat(actualUser).isNotNull();
        assertThat(user.getName()).isEqualTo(actualUser.getName());
        assertThat(user.getPassword()).isEqualTo(actualUser.getPassword());
        assertThat(user.getEmail()).isEqualTo(actualUser.getEmail());

    }
}