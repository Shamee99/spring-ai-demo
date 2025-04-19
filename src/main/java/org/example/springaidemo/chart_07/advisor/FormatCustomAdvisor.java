package org.example.springaidemo.chart_07.advisor;

import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;


public class FormatCustomAdvisor implements CallAroundAdvisor {


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

    private AdvisedRequest before(AdvisedRequest advisedRequest){
        System.out.println("[FormatCustomAdvisor] - 前置 AdvisorRequest 增强");
        return advisedRequest;
    }

    private void observeAfter(AdvisedResponse advisedResponse){
        System.out.println("[FormatCustomAdvisor] - 后置 advisedResponse 增强");
    }

    @Override
    public String getName() {
        return "FormatCustomAdvisor";
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 1;
    }
}
