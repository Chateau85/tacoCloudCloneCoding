package tacos.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import tacos.Order;
import tacos.Taco;

@Repository
public class JdbcOrderRepository implements OrderRepository {
	private final SimpleJdbcInsert orderInserter;
	private final SimpleJdbcInsert orderTacoInserter;

	public JdbcOrderRepository(JdbcTemplate jdbc) {
		this.orderInserter = new SimpleJdbcInsert(jdbc).withTableName("Taco_Order").usingGeneratedKeyColumns("id");
		this.orderTacoInserter = new SimpleJdbcInsert(jdbc).withTableName("Taco_Order_Tacos");
	}

	@Override
	public Order save(Order order) {
		order.setPlacedAt(new Date());
		long orderId = saveOrderDetails(order);
		order.setId(orderId);
		List<Taco> tacos = order.getTacos();
		for (Taco taco : tacos) {
			saveTacoToOrder(taco, orderId);
		}
		return order;
	}

	private void saveTacoToOrder(Taco taco, long orderId) {
		Map<String, Object> values = new HashMap<>();
		values.put("tacoOrder", orderId);
		values.put("taco", taco.getId());
		orderTacoInserter.execute(values);

	}

	private long saveOrderDetails(Order order) {
		Map<String, Object> values = new HashMap<>();
		values.put("deliveryName", order.getDeliveryName());
		values.put("deliveryStreet", order.getDeliveryStreet());
		values.put("deliveryCity", order.getDeliveryCity());
		values.put("deliveryState", order.getDeliveryState());
		values.put("deliveryZip", order.getDeliveryZip());
		values.put("ccNumber", order.getCcNumber());
		values.put("ccExpiration", order.getCcExpiration());
		values.put("ccCVV", order.getCcCVV());
		values.put("placedAt", order.getPlacedAt());
		return orderInserter.executeAndReturnKey(values).longValue();
	}

}
