package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.DataDictionaryVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ParamConst;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.ComplaintManage;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.HandleResult;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.ComplaintImportanceEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.MessageMenuEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.ComplaintManageMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.HandleResultMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ComplaintManageService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.mapper.RoleMenuMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.*;
import net.cdsunrise.hy.message.center.autoconfigure.feign.MessageCenterFeignClient;
import net.cdsunrise.hy.message.center.autoconfigure.feign.enums.AppEnum;
import net.cdsunrise.hy.message.center.autoconfigure.feign.enums.CategoryEnum;
import net.cdsunrise.hy.message.center.autoconfigure.feign.req.MessageRequest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author LHY
 */
@Service
@Slf4j
public class ComplaintManageServiceImpl implements ComplaintManageService{

    private ComplaintManageMapper manageMapper;
    private HandleResultMapper resultMapper;
    private DataDictionaryFeignClient feignClient;
    private MessageCenterFeignClient messageCenterFeignClient;
    private RoleMenuMapper roleMenuMapper;

    public ComplaintManageServiceImpl(ComplaintManageMapper manageMapper, HandleResultMapper resultMapper, DataDictionaryFeignClient feignClient, MessageCenterFeignClient messageCenterFeignClient, RoleMenuMapper roleMenuMapper) {
        this.manageMapper = manageMapper;
        this.resultMapper = resultMapper;
        this.feignClient = feignClient;
        this.messageCenterFeignClient = messageCenterFeignClient;
        this.roleMenuMapper = roleMenuMapper;
    }

    @Override
    public Long add(ComplaintManage complaintManage) {
        manageMapper.insert(complaintManage);
        try {
            // 推送消息
            String[] codeParam = {complaintManage.getChannel(),complaintManage.getType()};
            Map<String, DataDictionaryVO> map = feignClient.getByCodes(codeParam).getData();
            MessageRequest.AddReq addReq = new MessageRequest.AddReq();
            addReq.setApp(AppEnum.OPERATION);
            addReq.setCategory(CategoryEnum.TODO);
            addReq.setType("投诉管理 - 待受理");
            addReq.setMenuUri(MessageMenuEnum.OPERATION_COMPLAINT_NOT_HANDLE.getUri());
            addReq.setRedirect(true);
            addReq.setAggs(complaintManage.getComplaintObject());
            addReq.setBrief(map.get(complaintManage.getType()).getName());
            addReq.setBtime(complaintManage.getComplaintTime().getTime());
            addReq.setDetail("投诉内容："+complaintManage.getContent()+"，来源："+map.get(complaintManage.getChannel()).getName());
            List<Long> users = roleMenuMapper.getUserIdByMenuCode(MessageMenuEnum.OPERATION_COMPLAINT_NOT_HANDLE.getCode());
            addReq.setUserIds(users);
            messageCenterFeignClient.add(addReq);
        } catch (Exception e) {
            log.error("推送消息失败", e);
        }
        return complaintManage.getId();
    }

    @Override
    public ComplaintManage findById(Long id) {
        ComplaintManage complaintManage = manageMapper.selectById(id);
        // 通过数据字典进行编码转换
        String[] codeParam = {complaintManage.getChannel(),complaintManage.getType()};
        Map<String, DataDictionaryVO> map = feignClient.getByCodes(codeParam).getData();
        complaintManage.setChannel(map.get(complaintManage.getChannel()).getName());
        complaintManage.setType(map.get(complaintManage.getType()).getName());
        return complaintManage;
    }

    @Override
    public ComplaintManage getById(Long id) {
        return manageMapper.selectById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        UpdateWrapper<ComplaintManage> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        ComplaintManage complaintManage = new ComplaintManage();
        complaintManage.setStatus(status);
        manageMapper.update(complaintManage,updateWrapper);
    }

    @Override
    public Page<ComplaintManage> list(PageRequest<ComplaintManageCondition> pageRequest) {
        Page<ComplaintManage> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        ComplaintManageCondition condition = pageRequest.getCondition();
        if (condition == null){
            condition = new ComplaintManageCondition();
        }
        QueryWrapper<ComplaintManage> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .like(StringUtils.isNotEmpty(condition.getComplaintNumber()),ComplaintManage::getComplaintNumber,
                        condition.getComplaintNumber())
                .eq(StringUtils.isNotEmpty(condition.getType()),ComplaintManage::getType,condition.getType())
                .eq(condition.getStatus() != null,ComplaintManage::getStatus,condition.getStatus())
                .orderByAsc(ComplaintManage::getStatus);
        IPage<ComplaintManage> pageResource =  manageMapper.selectPage(page,wrapper);
        Long count = pageResource.getTotal();
        List<ComplaintManage> resourceList = pageResource.getRecords();
        List<ComplaintManage> collect = resourceList.stream().peek(complaintManage -> {
            // 通过数据字典进行编码转换
            String[] codeParam = {complaintManage.getChannel(),complaintManage.getType()};
            Map<String, DataDictionaryVO> map = feignClient.getByCodes(codeParam).getData();
            complaintManage.setChannel(map.get(complaintManage.getChannel()).getName());
            complaintManage.setType(map.get(complaintManage.getType()).getName());
        }).collect(Collectors.toList());
        page.setTotal(count);
        page.setRecords(collect);
        return page;
    }

    @Override
    public String generateComplaintNumber() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String currentTime = df.format(new Date());
        return ParamConst.COMPLAINT_NUMBER_PREFIX+currentTime+System.currentTimeMillis();
    }

    @Override
    @SuppressWarnings("all")
    public Map statisticsComplaint() {
        Map resultMap = new HashMap();
        QueryWrapper<ComplaintManage> queryWrapper = new QueryWrapper<>();
        resultMap.put("allCount",manageMapper.selectCount(queryWrapper));
        resultMap.put("notHandleCount",complaintCount(ParamConst.NOT_HANDLE_STATUS,null,null));
        resultMap.put("beingCount",complaintCount(ParamConst.BEING_HANDLE_STATUS,null,null));
        resultMap.put("rejectCount",complaintCount(ParamConst.REJECT_HANDLE_STATUS,null,null));
        resultMap.put("finishCount",complaintCount(ParamConst.FINISH_HANDLE_STATUS,null,null));
        return resultMap;
    }

    private Integer complaintCount(Integer status,String beginTime,String endTime){
        QueryWrapper<ComplaintManage> queryWrapper = new QueryWrapper<>();
        if (beginTime!=null && endTime!=null){
            queryWrapper.between("complaint_time",beginTime,endTime);
        }
        queryWrapper.eq("status",status);
        return manageMapper.selectCount(queryWrapper);
    }

    @Override
    @SuppressWarnings("all")
    public Map chartComplaint() {
        Map resultMap = new HashMap();
        String[] codeParam = {ParamConst.COMPLAINT_TYPE,ParamConst.COMPLAINT_CHANNEL,ParamConst.INDUSTRY_TYPE};
        Map<String, DataDictionaryVO> dataMap = feignClient.getByCodes(codeParam).getData();
        QueryWrapper<ComplaintManage> queryWrapper = new QueryWrapper<>();
        Integer allCount = manageMapper.selectCount(queryWrapper);
        /**统计投诉类型**/
        queryWrapper.select("type","count(*) as num").groupBy("type");
        List<Map<String,Object>> complaintTypeList = manageMapper.selectMaps(queryWrapper);
        List complaintTypeResultList = new ArrayList();
        List<DataDictionaryVO> complaintTypeDataList = dataMap.get(ParamConst.COMPLAINT_TYPE).getChildren();
        for (DataDictionaryVO dataDictionaryVO : complaintTypeDataList){
            boolean complaintTypeFlag = false;
            for (Map<String,Object> map : complaintTypeList){
                String type = map.get("type").toString();
                if (type.equals(dataDictionaryVO.getCode())){
                    complaintTypeFlag = true;
                    Integer num = Integer.parseInt(map.get("num").toString());
                    complaintTypeResultList.add(new ChartVO(dataDictionaryVO.getName(),num));
                    break;
                }
            }
            if (!complaintTypeFlag){
                complaintTypeResultList.add(new ChartVO(dataDictionaryVO.getName(),0));
            }
        }
        /**清除上一个搜索条件，统计投诉来源**/
        queryWrapper = new QueryWrapper<>();
        queryWrapper.select("channel","count(*) as num").groupBy("channel");
        List<Map<String,Object>> complaintChannelList = manageMapper.selectMaps(queryWrapper);
        List complaintChannelResultList = new ArrayList();
        List<DataDictionaryVO> complaintChannelDataList = dataMap.get(ParamConst.COMPLAINT_CHANNEL).getChildren();
        for (DataDictionaryVO dataDictionaryVO : complaintChannelDataList){
            boolean complaintChannelFlag = false;
            for (Map<String,Object> map : complaintChannelList){
                String channel = map.get("channel").toString();
                if (channel.equals(dataDictionaryVO.getCode())){
                    complaintChannelFlag = true;
                    Integer num = Integer.parseInt(map.get("num").toString());
                    complaintChannelResultList.add(new ChartVO(dataDictionaryVO.getName(),num));
                    break;
                }
            }
            if (!complaintChannelFlag){
                complaintChannelResultList.add(new ChartVO(dataDictionaryVO.getName(),0));
            }
        }
        /**统计投诉行业**/
        QueryWrapper<HandleResult> handleQueryWrapper = new QueryWrapper<>();
        Integer handleAllCount = resultMapper.selectCount(handleQueryWrapper);
        handleQueryWrapper.select("industry_type","count(*) as num").groupBy("industry_type");
        List<Map<String,Object>> complaintIndustryList = resultMapper.selectMaps(handleQueryWrapper);
        List complaintIndustryResultList = new ArrayList();
        List<DataDictionaryVO> complaintIndustryDataList = dataMap.get(ParamConst.INDUSTRY_TYPE).getChildren();
        for (DataDictionaryVO dataDictionaryVO : complaintIndustryDataList){
            boolean complaintIndustryFlag = false;
            for (Map<String,Object> map : complaintIndustryList){
                String industryType = map.get("industry_type").toString();
                if (industryType.equals(dataDictionaryVO.getCode())){
                    complaintIndustryFlag = true;
                    Integer num = Integer.parseInt(map.get("num").toString());
                    complaintIndustryResultList.add(new ChartVO(dataDictionaryVO.getName(),num));
                    break;
                }
            }
            if (!complaintIndustryFlag){
                complaintIndustryResultList.add(new ChartVO(dataDictionaryVO.getName(),0));
            }
        }
        /**投诉重要性**/
        List<HandleResultMapper.ImportanceChartVO> importanceChartVOList = resultMapper.countGroupByImportance();
        Map<Integer, Integer> importanceMap = importanceChartVOList.stream()
                .collect(Collectors.toMap(HandleResultMapper.ImportanceChartVO::getImportance, HandleResultMapper.ImportanceChartVO::getCount));
        List<ChartVO> complaintImportanceResultList = Arrays.stream(ComplaintImportanceEnum.values()).map(item -> {
            return new ChartVO(item.getImportanceDesc(), importanceMap.getOrDefault(item.getImportance(), 0));
        }).collect(Collectors.toList());

        resultMap.put("complaintType", complaintTypeResultList);
        resultMap.put("complaintChannel", complaintChannelResultList);
        resultMap.put("complaintIndustryType", complaintIndustryResultList);
        resultMap.put("complaintImportance", complaintImportanceResultList);
        return resultMap;
    }

    @Override
    public Map<String,Object> conditionStatisticsComplaint(PageRequest<StatisticsComplaintCondition> pageRequest) {
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("complaintCount",manageMapper.count(pageRequest.getCondition()));
        Page<StatisticsVO> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        List<StatisticsVO> collect = statisticsComplaintList(pageRequest);
        /**size设置-1，方便不分页查询总数**/
        page.setTotal(manageMapper.statisticsList(0L,-1L,pageRequest.getCondition()).size());
        page.setRecords(collect);
        resultMap.put("pageInfo",page);
        return resultMap;
    }

    @Override
    public List<StatisticsVO> statisticsComplaintList(PageRequest<StatisticsComplaintCondition> pageRequest) {
        StatisticsComplaintCondition condition = pageRequest.getCondition();
        Long startIndex = (pageRequest.getCurrent()-1)*pageRequest.getSize();
        List<StatisticsVO> list = manageMapper.statisticsList(startIndex,pageRequest.getSize(),condition);
        return list.stream().peek(statisticsVO -> {
            Map<String, DataDictionaryVO> map = new HashMap<>();
            if (StringUtils.isNotEmpty(statisticsVO.getIndustryType())){
                // 通过数据字典进行编码转换
                String[] codeParam = {statisticsVO.getChannel(),statisticsVO.getType(),statisticsVO.getIndustryType()};
                map = feignClient.getByCodes(codeParam).getData();
                DataDictionaryVO industryType = map.get(statisticsVO.getIndustryType());
                statisticsVO.setIndustryType(industryType!=null?industryType.getName():"");
            }else{
                String[] codeParam = {statisticsVO.getChannel(),statisticsVO.getType()};
                map = feignClient.getByCodes(codeParam).getData();
            }
            DataDictionaryVO channel = map.get(statisticsVO.getChannel());
            statisticsVO.setChannel(channel!=null?channel.getName():"");
            DataDictionaryVO type = map.get(statisticsVO.getType());
            statisticsVO.setType(type!=null?type.getName():"");
        }).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<byte[]> export(PageRequest<StatisticsComplaintCondition> pageRequest) {
        String excelName = "产业运行监测投诉统计";
        ClassPathResource classPathResource = new ClassPathResource("template/产业运行监测投诉统计模板.xlsx");
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
            Cell dateStringCell = headRow.getCell(4);
            dateStringCell.setCellValue(dateString);
            // 写入每一行数据
            int startRowIndex = 5;
            int rowIndex = startRowIndex;
            int cellNumber = 5;
            List<StatisticsVO> complaintList = statisticsComplaintList(pageRequest);
            if (!CollectionUtils.isEmpty(complaintList)) {
                Map<Integer, CellStyle> cellStyleMap = new HashMap<>(cellNumber);
                for (StatisticsVO statisticsVO : complaintList) {
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
                    row.getCell(0).setCellValue(statisticsVO.getCount());
                    row.getCell(1).setCellValue(statisticsVO.getType());
                    row.getCell(2).setCellValue(statisticsVO.getIndustryType());
                    row.getCell(3).setCellValue(statisticsVO.getChannel());
                    row.getCell(4).setCellValue(statisticsVO.getStatisticsTime());
                    rowIndex++;
                }
            }
            workbook.write(byteArrayOutputStream);
            // 下载
            byte[] body = byteArrayOutputStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename("产业运行监测.xlsx", StandardCharsets.UTF_8)
                    .build();
            headers.setContentDisposition(contentDisposition);
            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /** 通过商户名查询已完成的投诉单 **/
    @Override
    public List<MerchantComplaintVO> findByComplaintObject(String complaintObject) {
        List<MerchantComplaintVO> list = manageMapper.merchantComplaintList(complaintObject);
        return list.stream().peek(merchantComplaintVO -> {
            // 通过数据字典进行编码转换
            String[] codeParam = {merchantComplaintVO.getChannel()};
            Map<String, DataDictionaryVO> map = feignClient.getByCodes(codeParam).getData();
            merchantComplaintVO.setChannel(map.get(merchantComplaintVO.getChannel()).getName());
        }).collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("all")
    public Map statisticsComplaintByTime(String beginTime,String endTime) {
        Map resultMap = new HashMap();
        QueryWrapper<ComplaintManage> queryWrapper = new QueryWrapper<>();
        if (beginTime!=null && endTime!=null){
            queryWrapper.between("complaint_time",beginTime,endTime);
        }
        resultMap.put("allCount",manageMapper.selectCount(queryWrapper));
        resultMap.put("notHandleCount",complaintCount(ParamConst.NOT_HANDLE_STATUS,beginTime,endTime));
        resultMap.put("beingCount",complaintCount(ParamConst.BEING_HANDLE_STATUS,beginTime,endTime));
        resultMap.put("rejectCount",complaintCount(ParamConst.REJECT_HANDLE_STATUS,beginTime,endTime));
        resultMap.put("finishCount",complaintCount(ParamConst.FINISH_HANDLE_STATUS,beginTime,endTime));
        return resultMap;
    }

    @Override
    @SuppressWarnings("all")
    public Map chartComplaintByTime(String beginTime, String endTime) {
        Map resultMap = new HashMap();
        String[] codeParam = {ParamConst.COMPLAINT_TYPE,ParamConst.COMPLAINT_CHANNEL,ParamConst.INDUSTRY_TYPE};
        Map<String, DataDictionaryVO> dataMap = feignClient.getByCodes(codeParam).getData();
        QueryWrapper<ComplaintManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("complaint_time",beginTime,endTime);
        /**统计投诉类型**/
        queryWrapper.select("type","count(*) as num").groupBy("type");
        List<Map<String,Object>> complaintTypeList = manageMapper.selectMaps(queryWrapper);
        List complaintTypeResultList = new ArrayList();
        List<DataDictionaryVO> complaintTypeDataList = dataMap.get(ParamConst.COMPLAINT_TYPE).getChildren();
        for (DataDictionaryVO dataDictionaryVO : complaintTypeDataList){
            boolean complaintTypeFlag = false;
            for (Map<String,Object> map : complaintTypeList){
                String type = map.get("type").toString();
                if (type.equals(dataDictionaryVO.getCode())){
                    complaintTypeFlag = true;
                    Integer num = Integer.parseInt(map.get("num").toString());
                    complaintTypeResultList.add(new ChartVO(dataDictionaryVO.getName(),num));
                    break;
                }
            }
            if (!complaintTypeFlag){
                complaintTypeResultList.add(new ChartVO(dataDictionaryVO.getName(),0));
            }
        }
        /**清除上一个搜索条件，统计投诉来源**/
        queryWrapper = new QueryWrapper<>();
        queryWrapper.between("complaint_time",beginTime,endTime);
        queryWrapper.select("channel","count(*) as num").groupBy("channel");
        List<Map<String,Object>> complaintChannelList = manageMapper.selectMaps(queryWrapper);
        List complaintChannelResultList = new ArrayList();
        List<DataDictionaryVO> complaintChannelDataList = dataMap.get(ParamConst.COMPLAINT_CHANNEL).getChildren();
        for (DataDictionaryVO dataDictionaryVO : complaintChannelDataList){
            boolean complaintChannelFlag = false;
            for (Map<String,Object> map : complaintChannelList){
                String channel = map.get("channel").toString();
                if (channel.equals(dataDictionaryVO.getCode())){
                    complaintChannelFlag = true;
                    Integer num = Integer.parseInt(map.get("num").toString());
                    complaintChannelResultList.add(new ChartVO(dataDictionaryVO.getName(),num));
                    break;
                }
            }
            if (!complaintChannelFlag){
                complaintChannelResultList.add(new ChartVO(dataDictionaryVO.getName(),0));
            }
        }
        /**统计投诉行业**/
        List<Map<String,Object>> complaintIndustryList = manageMapper.queryIndustryByTime(beginTime, endTime);
        List complaintIndustryResultList = new ArrayList();
        List<DataDictionaryVO> complaintIndustryDataList = dataMap.get(ParamConst.INDUSTRY_TYPE).getChildren();
        for (DataDictionaryVO dataDictionaryVO : complaintIndustryDataList){
            boolean complaintIndustryFlag = false;
            for (Map<String,Object> map : complaintIndustryList){
                String industryType = map.get("industry_type").toString();
                if (industryType.equals(dataDictionaryVO.getCode())){
                    complaintIndustryFlag = true;
                    Integer num = Integer.parseInt(map.get("num").toString());
                    complaintIndustryResultList.add(new ChartVO(dataDictionaryVO.getName(),num));
                    break;
                }
            }
            if (!complaintIndustryFlag){
                complaintIndustryResultList.add(new ChartVO(dataDictionaryVO.getName(),0));
            }
        }
        resultMap.put("complaintType",complaintTypeResultList);
        resultMap.put("complaintChannel",complaintChannelResultList);
        resultMap.put("complaintIndustryType",complaintIndustryResultList);
        return resultMap;
    }

    @Override
    public List<ChartVO> complaintTrend(String beginTime,String endTime) {
        List<ChartVO> trend = manageMapper.complaintTrend(beginTime,endTime);
        return buildMixData(trend,beginTime.substring(0,7),endTime.substring(0,7));
    }

    // 根据起始日期月份，构建完整数据
    private List<ChartVO> buildMixData(List<ChartVO> list, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<>();
        List<String> dateList = null;
        try {
            dateList = DateUtil.getBetweenMonth(startTime, endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<ChartVO> dataList = new ArrayList<>();
        list.forEach(scenicTouristVO -> {
            paramMap.put(scenicTouristVO.getName(), scenicTouristVO.getValue());
        });
        for (String date : dateList) {
            ChartVO chartVO;
            if (paramMap.containsKey(date)) {
                chartVO = new ChartVO(date,Integer.parseInt(String.valueOf(paramMap.get(date))));
            } else {
                chartVO = new ChartVO(date,0);
            }
            dataList.add(chartVO);
        }
        return dataList;
    }
}
