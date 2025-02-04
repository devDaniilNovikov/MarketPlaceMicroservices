package dn.mp_orders.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class KafkaRecord {

    @Schema(name = "id",description = "Уникальный идентификатор сообщения в брокере сообщений")
    private String id;

    @Schema(name = "message",description = "Сообщение , которое передается в брокер сообщений")
    private String message;

    @Schema(name = "status",description = "Статус заказа")
    private String status;

    @Schema(name = "name",description = "Название заказа")
    private String name;

    @Override
    public String toString() {
        return "KafkaDto{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", name='" + name + '\'' +
                '}';
    }
}
