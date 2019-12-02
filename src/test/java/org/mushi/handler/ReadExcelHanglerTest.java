package org.mushi.handler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mushi.model.UserWorkInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.Set;


@SpringBootTest
@RunWith(SpringRunner.class)
public class ReadExcelHanglerTest {

    @Autowired
    ReadExcelHangler readExcelHangler;

    @Test
    public void readExcel() {
        Map<String, Object> stringObjectMap = readExcelHangler.readExcel();
        System.out.println(stringObjectMap);
    }
}