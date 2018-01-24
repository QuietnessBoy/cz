package test.com.tech.inter.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by wind on 2016/12/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ActiveProfiles("pro")
//@ContextConfiguration(locations = {"classpath*:spring/spring.pro.xml"})
@ActiveProfiles("dev")
@ContextConfiguration(locations = {"classpath*:spring/spring.dev.xml"})
@Rollback
public class BaseTest {

    @Test
    public void dummy() {
    }

}
