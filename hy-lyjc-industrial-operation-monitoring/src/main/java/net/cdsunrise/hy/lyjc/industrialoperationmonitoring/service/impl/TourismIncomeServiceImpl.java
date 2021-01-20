package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.TourismIncome;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.TourismIncomeMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.TourismIncomeService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TourismIncomeCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TourismIncomeVO;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: LHY
 * @Date: 2019/9/17 17:39
 */
@Service
public class TourismIncomeServiceImpl implements TourismIncomeService{

    @Autowired
    private TourismIncomeMapper tourismIncomeMapper;

    @Override
    @SuppressWarnings("all")
    public Map statisticsData(TourismIncomeCondition condition) {
        Map resultMap = new HashMap();
        /**节日旅游收入**/
        Double incomeAmount = incomeCount(condition.getStartTime(),condition.getEndTime(),null,null);
        resultMap.put("incomeAmount",incomeAmount);
        /**去年节日旅游收入**/
        Double lastIncomeAmount = incomeCount(condition.getLastStartTime(),condition.getLastEndTime(),null,null);
        if (lastIncomeAmount>0){
            // 保留两位小数
            Double compareAmount = new BigDecimal(incomeAmount * 1.0 / lastIncomeAmount)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            resultMap.put("compareAmount",compareAmount);
            /**去年节假日旅游收入趋势**/
            resultMap.put("lastIncomeTrend",tourismIncomeMapper.lastIncomeTrend(condition.getLastStartTime(),condition.getLastEndTime()));
        }
        /**今年节假日旅游收入趋势**/
        resultMap.put("tourismTrend",tourismIncomeMapper.incomeTrend(condition.getStartTime(),condition.getEndTime()));
        return resultMap;
    }

    @Override
    public Page<TourismIncomeVO> conditionStatisticsData(PageRequest<TourismIncomeCondition> pageRequest) {
        Page<TourismIncomeVO> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        TourismIncomeCondition condition = pageRequest.getCondition();
        if (condition == null){
            condition = new TourismIncomeCondition();
        }
        Long startIndex = (pageRequest.getCurrent()-1)*pageRequest.getSize();
        List<TourismIncomeVO> list = tourismIncomeMapper.statisticsList(startIndex,pageRequest.getSize(),condition);
        page.setRecords(list);
        /**方便不分页查询总数**/
        page.setTotal(tourismIncomeMapper.statisticsList(startIndex,-1L,condition).size());
        return page;
    }

    @SuppressWarnings("all")
    @Override
    public Map<String, Object> historyConditionStatisticsData(PageRequest<TourismIncomeCondition> pageRequest) {
        Map<String,Object> resultMap = new HashMap<>();
        Page<TourismIncomeVO> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        TourismIncomeCondition condition = pageRequest.getCondition();
        if (condition == null){
            condition = new TourismIncomeCondition();
        }
        Long startIndex = (pageRequest.getCurrent()-1)*pageRequest.getSize();
        List<TourismIncomeVO> list = tourismIncomeMapper.historyStatisticsList(startIndex,pageRequest.getSize(),condition);
        page.setRecords(list);
        /**方便不分页查询总数**/
        page.setTotal(tourismIncomeMapper.historyStatisticsList(startIndex,-1L,condition).size());
        resultMap.put("dataList",page);
        resultMap.put("incomeCount",incomeCount(condition.getStartTime(),condition.getEndTime(),condition.getScenicName(),condition.getIncomeSource()));
        return resultMap;
    }

    @Override
    public ResponseEntity<byte[]> export(PageRequest<TourismIncomeCondition> pageRequest) {
        String excelName = "产业运行监测旅游收入统计";
        ClassPathResource classPathResource = new ClassPathResource("template/产业运行监测旅游收入统计模板.xlsx");
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
            String dateString = DateUtil.format(pageRequest.getCondition().getStartTime(),DateUtil.PATTERN_YYYY_MM_DD) + " - " +
                    DateUtil.format(pageRequest.getCondition().getEndTime(),DateUtil.PATTERN_YYYY_MM_DD);
            Cell dateStringCell = headRow.getCell(3);
            dateStringCell.setCellValue(dateString);
            // 写入每一行数据
            int startRowIndex = 5;
            int rowIndex = startRowIndex;
            int cellNumber = 4;
            List<TourismIncomeVO> dataResourceList = tourismIncomeMapper.statisticsList(0L,-1L,pageRequest.getCondition());
            if (!CollectionUtils.isEmpty(dataResourceList)) {
                Map<Integer, CellStyle> cellStyleMap = new HashMap<>(cellNumber);
                for (TourismIncomeVO tourismIncomeVO : dataResourceList) {
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
                    row.getCell(0).setCellValue(tourismIncomeVO.getCount());
                    row.getCell(1).setCellValue(tourismIncomeVO.getIncomeSource());
                    row.getCell(2).setCellValue(tourismIncomeVO.getScenicName());
                    row.getCell(3).setCellValue(tourismIncomeVO.getStatisticsTime());
                    rowIndex++;
                }
            }
            workbook.write(byteArrayOutputStream);
            // 下载
            byte[] body = byteArrayOutputStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename("产业运行监测旅游收入统计.xlsx", StandardCharsets.UTF_8)
                    .build();
            headers.setContentDisposition(contentDisposition);
            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> historyExport(PageRequest<TourismIncomeCondition> pageRequest) {
        String excelName = "产业运行监测旅游收入历史统计";
        ClassPathResource classPathResource = new ClassPathResource("template/产业运行监测旅游收入历史统计模板.xlsx");
        try{
            InputStream inputStream = classPathResource.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            // 获取sheet
            Sheet sheet = workbook.getSheet(excelName);
            // 写入头部信息
            Row headRow = sheet.getRow(2);
            // 时间范围
            String dateString = DateUtil.format(pageRequest.getCondition().getStartTime(),DateUtil.PATTERN_YYYY_MM_DD) + " - " +
                    DateUtil.format(pageRequest.getCondition().getEndTime(),DateUtil.PATTERN_YYYY_MM_DD);
            // 对象名称
            Cell timeCell = headRow.getCell(1);
            timeCell.setCellValue(dateString);
            // 总收入金额
            TourismIncomeCondition condition = pageRequest.getCondition();
            if (condition == null){
                condition = new TourismIncomeCondition();
            }
            Cell incomeCell = headRow.getCell(3);
            incomeCell.setCellValue(incomeCount(condition.getStartTime(),condition.getEndTime(),condition.getScenicName(),condition.getIncomeSource()));
            // 写入每一行数据
            int startRowIndex = 5;
            int rowIndex = startRowIndex;
            int cellNumber = 4;
            List<TourismIncomeVO> dataResourceList = tourismIncomeMapper.statisticsList(0L,-1L,pageRequest.getCondition());
            if (!CollectionUtils.isEmpty(dataResourceList)) {
                Map<Integer, CellStyle> cellStyleMap = new HashMap<>(cellNumber);
                for (TourismIncomeVO tourismIncomeVO : dataResourceList) {
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
                    row.getCell(0).setCellValue(tourismIncomeVO.getCount());
                    row.getCell(1).setCellValue(tourismIncomeVO.getIncomeSource());
                    row.getCell(2).setCellValue(tourismIncomeVO.getScenicName());
                    row.getCell(3).setCellValue(tourismIncomeVO.getStatisticsTime());
                    rowIndex++;
                }
            }
            workbook.write(byteArrayOutputStream);
            // 下载
            byte[] body = byteArrayOutputStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename("产业运行监测旅游收入历史统计.xlsx", StandardCharsets.UTF_8)
                    .build();
            headers.setContentDisposition(contentDisposition);
            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private Double incomeCount(Timestamp startTime, Timestamp endTime,String scenicName,String incomeSource){
        QueryWrapper<TourismIncome> wrapper = new QueryWrapper<>();
        wrapper.select("SUM(income)")
                .between("time",startTime,endTime)
                .eq(StringUtils.isNotEmpty(scenicName),"scenic_name",scenicName)
                .eq(StringUtils.isNotEmpty(incomeSource),"income_source", incomeSource);
        List<Object> list = tourismIncomeMapper.selectObjs(wrapper);
        return list.get(0)!=null?Double.parseDouble(list.get(0).toString()):0;
    }
}
