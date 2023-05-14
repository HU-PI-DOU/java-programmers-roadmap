package org.hupidou.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class LogAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain function. agentArgs = " + agentArgs);

        new AgentBuilder.Default()
                .type(ElementMatchers.any())
                .transform((builder, typeDescription, classLoader, module) -> {
                    return builder.visit(Advice.to(LogInterceptor.class).on(ElementMatchers.isMethod()));
                })
                .installOn(inst);
    }

    public static class LogInterceptor {
        @Advice.OnMethodEnter
        public static void enter(@Advice.Origin String method, @Advice.AllArguments Object[] args) {
            System.out.println("enter method: " + method + ", args: " + args);
        }
    }
}
