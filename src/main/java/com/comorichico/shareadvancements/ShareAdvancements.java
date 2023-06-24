package com.comorichico.shareadvancements;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public final class ShareAdvancements extends JavaPlugin implements Listener {

    private List<AdvancementData> advancements = new ArrayList<>();
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getAdvancementData();
    }

    public void getAdvancementData() {
        Gson gson = new Gson();

        InputStreamReader reader = new InputStreamReader(
                ShareAdvancements.class.getClassLoader().getResourceAsStream("ja_jp.json"),
                StandardCharsets.UTF_8
        );

        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

        if (jsonObject != null) {
            for (String key : jsonObject.keySet()) {
                if (key.startsWith("advancements") && key.endsWith("title")) {
                    String value = jsonObject.get(key).getAsString();
                    AdvancementData data = new AdvancementData(key.replace("advancements.", "").replace(".title", "").replace(".", "/"), value, null, null, false);
                    this.advancements.add(data);
                }
            }
        }
    }

    public void setAdvancementData(Player player){
        for(AdvancementData advancementData: this.advancements){
            // 進捗オブジェクトを取得する
            Advancement advancement = Bukkit.getServer().getAdvancement(NamespacedKey.minecraft(advancementData.getKey()));
            if(null != advancement){
                // プレイヤーの進捗オブジェクトを取得する
                AdvancementProgress progress = player.getAdvancementProgress(advancement);

                // 進捗の進行度をセットする
                advancementData.setAwardedCriteria(progress.getAwardedCriteria());
                advancementData.setRemainingCriteria(progress.getRemainingCriteria());
                advancementData.setDone(progress.isDone());
            }
        }
    }
    @EventHandler
    public void onPlayerAdvancementCriterionGrant(PlayerAdvancementCriterionGrantEvent event) {
        ///advancement revoke @a everything

        setAdvancementData(event.getPlayer());
        for(AdvancementData advancementData: this.advancements){
            if(advancementData.isDone()){
                event.getPlayer().sendMessage(Component.text("達成済み：" + advancementData.getValue()));
            } else {
                if(null != advancementData.getAwardedCriteria()){
                    for(String s: advancementData.getAwardedCriteria()){
                        event.getPlayer().sendMessage(Component.text("進捗途中：" + advancementData.getValue() + "：" + s));
                        for(World w: Bukkit.getServer().getWorlds()){
                            for(Player p: w.getPlayers()){
                                Advancement advancement = Bukkit.getServer().getAdvancement(NamespacedKey.minecraft(advancementData.getKey()));
                                if(null != advancement){
                                    AdvancementProgress progress = p.getAdvancementProgress(advancement);
                                    progress.awardCriteria(s);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
