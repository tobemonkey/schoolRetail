package edu.hour.schoolretail;

import com.github.jeffreyning.mybatisplus.conf.EnableMPP;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = "edu.hour.schoolretail.mapper")
@EnableScheduling
@EnableMPP
public class SchoolRetailApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolRetailApplication.class, args);
    }

}
