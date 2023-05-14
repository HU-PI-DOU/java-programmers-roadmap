package org.hupidou.action;

import org.hupidou.alg.DAG;
import org.hupidou.alg.DAGScheduler;
import org.junit.jupiter.api.Test;

public class TopologyTaskScheduleTest {

    @Test
    void testSchedule() {
        DAG dag = new DAG();

        dag.addTask("A");
        dag.addTask("B");
        dag.addTask("C");
        dag.addTask("D");
        dag.addTask("E");

        dag.addDependency("A", "B");
        dag.addDependency("A", "C");
        dag.addDependency("B", "D");
        dag.addDependency("C", "D");
        dag.addDependency("D", "E");

        DAGScheduler scheduler = new DAGScheduler();
        scheduler.schedule(dag);
    }

    @Test
    void testSchedule2() {
        DAG dag = new DAG();
        // 定义5个任务
        dag.addTask("A");
        dag.addTask("B");
        dag.addTask("C");
        dag.addTask("D");
        dag.addTask("E");
        // 定义依赖关系
        dag.addDependency("B", "C");
        dag.addDependency("B", "A");
        dag.addDependency("C", "D");
        dag.addDependency("D", "E");

        DAGScheduler scheduler = new DAGScheduler();
        scheduler.schedule(dag);
    }
}
