package com.example.algorithm.alibaba;

import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 写一个邮件查找算法， 可以根据发件人+标题关键字+时间区间中的任意一个或者几个进行搜索，
 * 返回符合查找条件的邮件列表
 * 要点：
 * 1、对查询条件对象和邮件对象分别建模
 * 2、需要支持灵活组合的查询条件
 * 3、须具备一定的扩展性，支持未来更多的条件维度
 * @author  gsh
 * @date  2020/4/26 下午8:35
 **/
public class MailSearchTest {

    /**
     *
     * @author  gsh
     * @date  2020/4/26 下午8:40
     * @Param
     * @return
     **/
    public List<MailModel> searchByCondition(List<MailModel> mailList, ConditionModel conditionModel) {
        List<MailModel> selectedList = Lists.newArrayList();
        if (conditionModel == null) {
            return null;
        }
        boolean isSelected = false;
        for (MailModel mailModel : mailList) {
            if (StringUtils.isNotBlank(conditionModel.subjectFrom) && mailModel.getSubjectFrom().equals(conditionModel.subjectFrom)) {
                isSelected = true;
            }
            //其他条件类似 ...
            if (isSelected) {
                selectedList.add(mailModel);
            }
        }
        return selectedList;
    }


    /**
     * 邮件对象
     */
    @Data
    public static class MailModel {
        private String subjectFrom;
        private String title;
        private Date sendTime;
    }

    /**
     * 查询条件对象
     */
    @Data
    public static class ConditionModel {
        private String subjectFrom;
        private String title;
        private Date startTime;
        private Date endTime;
    }

}
