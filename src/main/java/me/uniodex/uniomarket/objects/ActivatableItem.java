package me.uniodex.uniomarket.objects;

import lombok.Getter;
import me.uniodex.unioessentials.objects.DatabaseInfo;

import java.util.List;

public class ActivatableItem {

    @Getter
    private String itemName;
    @Getter
    private String displayName;
    @Getter
    private DatabaseInfo databaseInfo;
    @Getter
    private String itemColumn;
    @Getter
    private int spaceNeeded;
    @Getter
    private List<String> rewardCommands;

    public ActivatableItem(String itemName, String displayName, DatabaseInfo databaseInfo, String itemColumn, int spaceNeeded, List<String> rewardCommands) {
        this.itemName = itemName;
        this.displayName = displayName;
        this.databaseInfo = databaseInfo;
        this.itemColumn = itemColumn;
        this.spaceNeeded = spaceNeeded;
        this.rewardCommands = rewardCommands;
    }


}
