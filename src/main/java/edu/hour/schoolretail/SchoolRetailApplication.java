package edu.hour.schoolretail;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import edu.hour.schoolretail.util.EmailUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.mail.MessagingException;

@SpringBootApplication
@MapperScan(basePackages = "edu.hour.schoolretail.mapper")
@EnableScheduling
public class SchoolRetailApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolRetailApplication.class, args);
    }

}
