package com.comorichico.shareadvancements;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdvancementData {
    private String key; // 進捗を表すキー
    private String value; // 進捗の内容
    private Collection<String> awardedCriteria; // 達成済みの進捗の集合
    private Collection<String> remainingCriteria; // 未達成の進捗の集合
    private boolean isDone; // 達成済みフラグ

    public AdvancementData(String key, String value, Collection<String> awardedCriteria, Collection<String> remainingCriteria, boolean isDone) {
        this.key = key;
        this.value = value;
        this.awardedCriteria = awardedCriteria;
        this.remainingCriteria = remainingCriteria;
        this.isDone = isDone;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Collection<String> getAwardedCriteria() {
        return awardedCriteria;
    }

    public Collection<String> getRemainingCriteria() {
        return remainingCriteria;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setAwardedCriteria(Collection<String> awardedCriteria) {
        if(null == this.awardedCriteria){
            this.awardedCriteria = awardedCriteria;
        } else {
            Stream.concat(this.awardedCriteria.stream(),awardedCriteria.stream())
                    .distinct()
                    .collect(Collectors.toList());
        }
    }

    public void setRemainingCriteria(Collection<String> remainingCriteria) {
        if(null == this.remainingCriteria){
            this.remainingCriteria = remainingCriteria;
        } else {
            Stream.concat(this.remainingCriteria.stream(),remainingCriteria.stream())
                    .distinct()
                    .collect(Collectors.toList());
        }
    }

    public void setDone(boolean isDone) {
        if(isDone){
            this.isDone = true;
        }
    }
}