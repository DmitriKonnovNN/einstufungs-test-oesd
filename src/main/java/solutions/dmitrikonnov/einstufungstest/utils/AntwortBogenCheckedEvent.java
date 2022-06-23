package solutions.dmitrikonnov.einstufungstest.utils;


import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AntwortBogenCheckedEvent extends ApplicationEvent {

    private final String CHECKED_MSG = "Antwortbogen has been checked. Cache is ready to get evicted";
    private final String tempResult;
    private final Long bogenId;

    public AntwortBogenCheckedEvent(Object source, Long bogenId ,String msg) {
        super(source);
        this.bogenId = bogenId;
        this.tempResult = msg;
    }
}
