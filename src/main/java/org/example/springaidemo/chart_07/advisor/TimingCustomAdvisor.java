package org.example.springaidemo.chart_07.advisor;

import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;


public class TimingCustomAdvisor implements CallAroundAdvisor {


    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        // 1. 前置增强
        advisedRequest = this.before(advisedRequest);

        // 2. 执行后续的advisor链
        AdvisedResponse response = chain.nextAroundCall(advisedRequest);
        // 3. 后置增强
        this.observeAfter(response);
        return response;
    }


    /**
     * 前置增强，记录一个初始时间，并将时间存放到advisorContext
     *
     * @param advisedRequest
     * @return
     */
    private AdvisedRequest before(AdvisedRequest advisedRequest){
        System.out.println("[TimingCustomAdvisor] - 前置 AdvisorRequest 增强");
        // 存储耗时，上文中我们提到AdvisorContext会共享状态。我们将耗时记录在上下文中，当然也可以自己定义一个变量存储
        advisedRequest.adviseContext().put("start0", System.currentTimeMillis());
        return advisedRequest;
    }

    /**
     * 后置增强，获取初始时间，并计算耗时
     * @param advisedResponse
     */
    private void observeAfter(AdvisedResponse advisedResponse){
        System.out.println("[TimingCustomAdvisor] - 后置 advisedResponse 增强");
        Long start0 = (Long) advisedResponse.adviseContext().get("start0");
        System.out.println("[TimingCustomAdvisor] - 从 AdvisorContext中获取start0:" + start0);
        System.out.println("[TimingCustomAdvisor] - 耗时：" + (System.currentTimeMillis() - start0));
    }


    /**
     * 指定当前advisor名称
     * @return
     */
    @Override
    public String getName() {
        return "TimingCustomAdvisor";
    }


    /**
     * 给定执行顺序
     * @return
     */
    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
