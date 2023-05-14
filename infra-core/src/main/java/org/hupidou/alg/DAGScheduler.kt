package org.hupidou.alg

import java.util.*

/**
 * V1版本: 基于DAG的有向无环图的任务调度和执行
 */
data class TaskNode(
    /*该任务的父节点列表*/val parents: MutableList<TaskNode>,
    /*该任务的子节点列表*/val children: MutableList<TaskNode>,
    /*任务名称*/val name: String
)

class DAG(

) {
    // DAG中所有的节点
    val nodes: MutableList<TaskNode> = mutableListOf()

    // 如果想做成服务，这个方法必须提供，只有提供了这个方法，才可用动态增加
    fun addTask(name: String) {
        // 创建一个新的任务节点，并添加到节点列表中
        nodes.add(TaskNode(mutableListOf(), mutableListOf(), name))
    }

    fun addDependency(parent: String, child: String) {
        // 在节点列表中查找父节点和子节点
        val parentNode = findNode(parent)
        val childNode = findNode(child)
        // 添加父子关系
        parentNode.children.add(childNode)
        childNode.parents.add(parentNode)
    }

    private fun findNode(name: String): TaskNode {
        // 在节点列表中查找指定节点
        for (node in nodes) {
            if (node.name == name) {
                return node
            }
        }
        throw IllegalArgumentException("Node not found: $name")
    }
}

class DAGScheduler {
    fun schedule(dag: DAG) {
        // 创建一个队列，用于保存未执行的任务节点
        val queue = LinkedList<TaskNode>()
        // 将没有任何前置任务的任务节点加入队列
        for (node in dag.nodes) {
            if (node.parents.isEmpty()) {
                queue.offer(node)
            }
        }
        // 不断从队列中取出任务节点，并执行该任务以及所有它的子孙节点
        while (!queue.isEmpty()) {
            val node = queue.poll()
            execute(node)

            for (child in node.children) {
                child.parents.remove(node)
                if (child.parents.isEmpty()) {
                    queue.offer(child)
                }
            }
        }


        // 所有任务都已执行完毕
        println("All tasks are completed!");

    }

    private fun execute(node: TaskNode) {
        // 执行该任务节点
        println("Execute task: ${node.name}")
    }
}