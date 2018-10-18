  import java.util.ArrayList
  import hudson.model.*;

  // Remove everything which is currently queued
  def q = Jenkins.instance.queue
  for (queued in Jenkins.instance.queue.items) {
    q.cancel(queued.task)
    println queued.task
  }

  // stop all the currently running jobs
  for (job in Jenkins.instance.items) {
    stopJobs(job)
    println job
  }

  def stopJobs(job) {
    if (job in com.cloudbees.hudson.plugins.folder.Folder) {
      for (child in job.items) {
        stopJobs(child)
        println child
      }    
    } else if (job in org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject) {
      for (child in job.items) {
        stopJobs(child)
        println child
      }
    } else if (job in org.jenkinsci.plugins.workflow.job.WorkflowJob) {

      if (job.isBuilding()) {
        for (build in job.builds) {
        build.doKill()
          println build
        }
      }
    }
  }