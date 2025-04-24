package com.baisanh.controller.admin;

import com.baisanh.result.Result;
import com.baisanh.service.ReportService;
import com.baisanh.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * 报表统计相关接口
 */
@RestController
@RequestMapping("/admin/report")
@Api(tags = "数据统计相关接口")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("begin") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("end") LocalDate end) {
        // 参数校验
        if (begin == null || end == null) {
            log.error("turnoverStatistics方法参数错误: begin={}, end={}", begin, end);
            return Result.error("日期参数不能为空");
        }

        // 如果开始日期大于结束日期，则交换
        if (begin.isAfter(end)) {
            LocalDate temp = begin;
            begin = end;
            end = temp;
            log.warn("turnoverStatistics入参日期顺序错误，已自动调整: begin={}, end={}", begin, end);
        }

        return Result.success(reportService.getTurnoverStatistics(begin, end));
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("begin") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("end") LocalDate end){
        // 参数校验
        if (begin == null || end == null) {
            log.error("userStatistics方法参数错误: begin={}, end={}", begin, end);
            return Result.error("日期参数不能为空");
        }

        // 如果开始日期大于结束日期，则交换
        if (begin.isAfter(end)) {
            LocalDate temp = begin;
            begin = end;
            end = temp;
            log.warn("userStatistics入参日期顺序错误，已自动调整: begin={}, end={}", begin, end);
        }

        return Result.success(reportService.getUserStatistics(begin, end));
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计")
    public Result<OrderReportVO> ordersStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("begin") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("end") LocalDate end){
        // 参数校验
        if (begin == null || end == null) {
            log.error("ordersStatistics方法参数错误: begin={}, end={}", begin, end);
            return Result.error("日期参数不能为空");
        }

        // 如果开始日期大于结束日期，则交换
        if (begin.isAfter(end)) {
            LocalDate temp = begin;
            begin = end;
            end = temp;
            log.warn("ordersStatistics入参日期顺序错误，已自动调整: begin={}, end={}", begin, end);
        }

        return Result.success(reportService.getOrderStatistics(begin, end));
    }

    /**
     * 销量top10
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/top10")
    @ApiOperation("销量top10")
    public Result<SalesTop10ReportVO> top10(
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("begin") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("end") LocalDate end) {
        // 参数校验
        if (begin == null || end == null) {
            log.error("top10方法参数错误: begin={}, end={}", begin, end);
            return Result.error("日期参数不能为空");
        }

        // 如果开始日期大于结束日期，则交换
        if (begin.isAfter(end)) {
            LocalDate temp = begin;
            begin = end;
            end = temp;
            log.warn("top10入参日期顺序错误，已自动调整: begin={}, end={}", begin, end);
        }

        return Result.success(reportService.getSalesTop10(begin, end));
    }

    /**
     * 导出运营数据报表
     * @param response
     */
    @GetMapping("/export")
    @ApiOperation("导出运营数据报表")
    public void export(HttpServletResponse response) {
        try {
            reportService.exportBusinessData(response);
        } catch (Exception e) {
            log.error("导出运营数据报表异常: ", e);
            // 这里可以设置HTTP错误状态码
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}