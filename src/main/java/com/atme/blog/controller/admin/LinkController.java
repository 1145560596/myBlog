package com.atme.blog.controller.admin;


import com.atme.blog.entity.Link;
import com.atme.blog.service.LinkService;
import com.atme.blog.utils.Result;
import com.atme.blog.utils.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author testjava
 * @since 2020-10-18
 */
@Controller
@RequestMapping("/admin/links")
public class LinkController {

    @Resource
    private LinkService linkService;

    @GetMapping("")
    public String linkPage(HttpServletRequest request) {
        request.setAttribute("path", "links");
        return "admin/link";
    }

    @GetMapping("/list")
    @ResponseBody
    public Result list(@RequestParam Map<String,Object> params) {
        if(StringUtils.isEmpty((String) params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        return ResultGenerator.getSuccessResult(linkService.getLinkList(params));
    }

    /**
     * 友链添加
     */
    @PostMapping("/save")
    public Result add(@RequestParam("linkType") Integer linkType,
                      @RequestParam("linkName") String linkName,
                      @RequestParam("linkUrl") String linkUrl,
                      @RequestParam("linkRank") Integer linkRank,
                      @RequestParam("linkDescription") String linkDescription) {
        if (linkType == null || linkType < 0 || linkRank == null || linkRank < 0 ||
                StringUtils.isEmpty(linkName) ||
                StringUtils.isEmpty(linkUrl) ||
                StringUtils.isEmpty(linkDescription)) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        Link link = new Link();
        link.setLinkType(linkType);
        link.setLinkRank(linkRank);
        link.setLinkName(linkName);
        link.setLinkUrl(linkUrl);
        link.setLinkDescription(linkDescription);
        return ResultGenerator.getSuccessResult(linkService.saveLink(link));
    }

    /**
     * 详情
     */
    @GetMapping("/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Integer id) {
        Link link = linkService.selectById(id);
        return ResultGenerator.getSuccessResult(link);
    }

    /**
     * 友链修改
     */
    @PostMapping(value = "update")
    @ResponseBody
    public Result update(@RequestParam("linkId") Integer linkId,
                         @RequestParam("linkType") Integer linkType,
                         @RequestParam("linkName") String linkName,
                         @RequestParam("linkUrl") String linkUrl,
                         @RequestParam("linkRank") Integer linkRank,
                         @RequestParam("linkDescription") String linkDescription) {
        Link tempLink = linkService.selectById(linkId);
        if (Objects.isNull(tempLink)) {
            return ResultGenerator.getFailResult("无数据！");
        }
        if (linkType == null || linkType < 0 || linkRank == null || linkRank < 0 ||
                StringUtils.isEmpty(linkName) ||
                StringUtils.isEmpty(linkUrl) ||
                StringUtils.isEmpty(linkDescription)) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        tempLink.setLinkType(linkType);
        tempLink.setLinkRank(linkRank);
        tempLink.setLinkName(linkName);
        tempLink.setLinkUrl(linkUrl);
        tempLink.setLinkDescription(linkDescription);
        return ResultGenerator.getSuccessResult(linkService.updateLink(tempLink));
    }

    /**
     * 友链删除
     */
    @PostMapping(value = "delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        if (linkService.deleteBatch(ids) == ids.length) {
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("删除失败");
        }
    }



}

