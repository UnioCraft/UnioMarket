package me.uniodex.uniomarket.hooks;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobsPlayer;
import lombok.Getter;
import me.uniodex.uniomarket.UnioMarket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class JobsHook {

    private UnioMarket plugin;
    @Getter
    private Jobs jobsPlugin;
    @Getter
    private Job minerJob;
    @Getter
    private Job hunterJob;

    public JobsHook(UnioMarket plugin) {
        this.plugin = plugin;
        this.jobsPlugin = (Jobs) Bukkit.getPluginManager().getPlugin("Jobs");

        // TODO sync names with jobs
        minerJob = Jobs.getJob("madenci");
        hunterJob = Jobs.getJob("avci");
    }

    public boolean isPlayerMiner(Player player) {
        JobsPlayer jobsPlayer = Jobs.getPlayerManager().getJobsPlayer(player);
        if (jobsPlayer == null) return false;
        return jobsPlayer.isInJob(minerJob);
    }

    public boolean isPlayerHunter(Player player) {
        JobsPlayer jobsPlayer = Jobs.getPlayerManager().getJobsPlayer(player);
        if (jobsPlayer == null) return false;
        return jobsPlayer.isInJob(hunterJob);
    }
}
