package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.TourismTrafficService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.feign.TrafficFeignService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.traffic.ExternalTableVO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LHY
 */
@Service
public class TourismTrafficServiceImpl implements TourismTrafficService{

    @Autowired
    private TrafficFeignService feignService;

    @Override
    public ResponseEntity<byte[]> export(Long beginTime, Long endTime, String provinceName, String cityName) {
        String excelName = "产业运行监测旅游交通统计";
        ClassPathResource classPathResource = new ClassPathResource("template/产业运行监测旅游交通统计模板.xlsx");
        try{
            InputStream inputStream = classPathResource.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            // 获取sheet
            Sheet sheet = workbook.getSheet(excelName);
            // 写入头部信息
            Row headRow = sheet.getRow(2);
            // 对象名称
            Cell summaryCell = headRow.getCell(1);
            summaryCell.setCellValue(excelName);
            // 时间范围
            String dateString = DateUtil.format(new Date(beginTime),DateUtil.PATTERN_YYYY_MM_DD) + " - " +
                    DateUtil.format(new Date(endTime),DateUtil.PATTERN_YYYY_MM_DD);
            Cell dateStringCell = headRow.getCell(3);
            dateStringCell.setCellValue(dateString);
            // 写入每一行数据
            int startRowIndex = 5;
            int rowIndex = startRowIndex;
            int cellNumber = 4;
            List<ExternalTableVO> dataList = feignService.getTable(beginTime, endTime, provinceName, cityName).getData();
            if (!CollectionUtils.isEmpty(dataList)) {
                Map<Integer, CellStyle> cellStyleMap = new HashMap<>(cellNumber);
                for (ExternalTableVO tableVO : dataList) {
                    Row row;
                    if (rowIndex == startRowIndex) {
                        row = sheet.getRow(rowIndex);
                    } else {
                        // 创建新的行
                        row = sheet.createRow(rowIndex);
                    }
                    // 复制样式
                    for (int i = 0; i < cellNumber; i++) {
                        Cell cell;
                        if (rowIndex == 5) {
                            // 模板行，保存样式
                            cell = row.getCell(i);
                            cellStyleMap.put(i, cell.getCellStyle());
                        } else {
                            // 复制样式
                            cell = row.createCell(i);
                            cell.setCellStyle(cellStyleMap.get(i));
                        }
                    }
                    row.getCell(0).setCellValue(tableVO.getFlow());
                    row.getCell(1).setCellValue(tableVO.getProvinceName());
                    row.getCell(2).setCellValue(tableVO.getCityName());
                    row.getCell(3).setCellValue(DateUtil.format(new Date(tableVO.getStatisticsTime()),DateUtil.PATTERN_YYYY_MM_DD));
                    rowIndex++;
                }
            }
            workbook.write(byteArrayOutputStream);
            // 下载
            byte[] body = byteArrayOutputStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename("产业运行监测旅游交通统计.xlsx", StandardCharsets.UTF_8)
                    .build();
            headers.setContentDisposition(contentDisposition);
            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> historyExport(Long beginTime, Long endTime, Integer graininess, String provinceName, String cityName) {
        String excelName = "产业运行监测旅游交通统计";
        ClassPathResource classPathResource = new ClassPathResource("template/产业运行监测旅游交通统计模板.xlsx");
        try{
            InputStream inputStream = classPathResource.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            // 获取sheet
            Sheet sheet = workbook.getSheet(excelName);
            // 写入头部信息
            Row headRow = sheet.getRow(2);
            // 对象名称
            Cell summaryCell = headRow.getCell(1);
            summaryCell.setCellValue(excelName);
            // 时间范围
            String dateString = DateUtil.format(new Date(beginTime),DateUtil.PATTERN_YYYY_MM_DD) + " - " +
                    DateUtil.format(new Date(endTime),DateUtil.PATTERN_YYYY_MM_DD);
            Cell dateStringCell = headRow.getCell(3);
            dateStringCell.setCellValue(dateString);
            // 写入每一行数据
            int startRowIndex = 5;
            int rowIndex = startRowIndex;
            int cellNumber = 4;
            List<ExternalTableVO> dataList = feignService.getByGraininess(beginTime, endTime, graininess, provinceName, cityName)
                    .getData().getExternalTableVOList();
            if (!CollectionUtils.isEmpty(dataList)) {
                Map<Integer, CellStyle> cellStyleMap = new HashMap<>(cellNumber);
                for (ExternalTableVO tableVO : dataList) {
                    Row row;
                    if (rowIndex == startRowIndex) {
                        row = sheet.getRow(rowIndex);
                    } else {
                        // 创建新的行
                        row = sheet.createRow(rowIndex);
                    }
                    // 复制样式
                    for (int i = 0; i < cellNumber; i++) {
                        Cell cell;
                        if (rowIndex == 5) {
                            // 模板行，保存样式
                            cell = row.getCell(i);
                            cellStyleMap.put(i, cell.getCellStyle());
                        } else {
                            // 复制样式
                            cell = row.createCell(i);
                            cell.setCellStyle(cellStyleMap.get(i));
                        }
                    }
                    row.getCell(0).setCellValue(tableVO.getFlow());
                    row.getCell(1).setCellValue(tableVO.getProvinceName());
                    row.getCell(2).setCellValue(tableVO.getCityName());
                    row.getCell(3).setCellValue(DateUtil.format(new Date(tableVO.getStatisticsTime()),DateUtil.PATTERN_YYYY_MM_DD));
                    rowIndex++;
                }
            }
            workbook.write(byteArrayOutputStream);
            // 下载
            byte[] body = byteArrayOutputStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename("产业运行监测旅游交通历史数据统计.xlsx", StandardCharsets.UTF_8)
                    .build();
            headers.setContentDisposition(contentDisposition);
            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
