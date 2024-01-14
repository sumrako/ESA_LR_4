package com.example.demo.dto;

import com.example.demo.data.Log;
import com.example.demo.data.Subscription;
import com.example.demo.enums.ChangeTypeEnum;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LetterDto {
    private Log log;
    private Subscription subscription;

    @Override
    public String toString() {
        StringBuilder letter = new StringBuilder();
        letter.append(subscription.getReceiver()).append(",").append(" уведомляем Вас о произошедших изменениях в данных.")
                .append("\n");
        if (log.getChangeType() == ChangeTypeEnum.INSERT) {
            letter.append("Были добавлены новые данные в таблицу ").append(log.getTable()).append(".").append("\n")
                    .append("Новая запись: ").append(log.getValue()).append("\n");
        }
        if (log.getChangeType() == ChangeTypeEnum.UPDATE) {
            letter.append("Были изменены данные в таблице ").append(log.getTable()).append(".").append("\n")
                    .append("Запись после изменений: ").append(log.getValue()).append("\n");
        }
        if (log.getChangeType() == ChangeTypeEnum.DELETE) {
            letter.append("Были удалены данные из таблицы ").append(log.getTable()).append(".").append("\n")
                    .append("Удаленная запись: ").append(log.getValue()).append("\n");
        }
        if (log.getChangeType() == ChangeTypeEnum.CASCADE_DELETE) {
            letter.append("Были удалены данные из таблицы ").append(log.getTable()).append(".").append("\n")
                    .append("Удаленная запись: ").append(log.getValue()).append("\n")
                    .append("Внимание! Это удаление повлекло за собой каскадное удаление записей из других таблиц.")
                    .append("\n");
        }
        letter.append("Время создания лога: ").append(log.getDatetime());
        return letter.toString();
    }
}
