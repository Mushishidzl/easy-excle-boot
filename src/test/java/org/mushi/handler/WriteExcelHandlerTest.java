package org.mushi.handler;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WriteExcelHandlerTest {

    @Autowired
    WriteExcelHandler writeExcelHandler;

    @Autowired
    ReadExcelHangler readExcelHangler;

    @Test
    public void exportToFile() {
        Map<String, Object> stringObjectMap = readExcelHangler.readExcel();
        writeExcelHandler.exportToFile("D:/demo.xlsx","123",stringObjectMap);
    }
}