package net.cdsunrise.hy.lyyt.controller;

import net.cdsunrise.hy.lyyt.annatation.DataType;
import net.cdsunrise.hy.lyyt.entity.vo.YdpInfoVO;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.service.YdpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName YdpController
 * @Description 诱导屏控制器
 * @Author LiuYin
 * @Date 2019/12/21 10:19
 */
@RestController
@RequestMapping("/ydp")
public class YdpController{

    private final YdpService ydpService;

    @Autowired
    public YdpController(YdpService ydpService) {
        this.ydpService = ydpService;
    }

    /**
     * 获取所有诱导屏信息
     * @return 诱导屏列表
     */
    @GetMapping("/all")
    @DataType({DataTypeEnum.SLYJJCSJ,DataTypeEnum.LYZXSJ})
    public List<YdpInfoVO> getAll(){
        return ydpService.getAll();
    }
}
