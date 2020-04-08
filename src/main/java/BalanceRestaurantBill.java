import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*
Payments :

Alice: 25
Bob: 20
John: 30
Mary: 45
Total -> 120
Each needs to pay -> 30

Expected result transactions :

Bob to Mary -> 10
Alice to Mary -> 5
*/

public class BalanceRestaurantBill {

  public static void main(String[] args) {
    Map<String, Float> map = new HashMap<>();

    map.put("Alice", 25f);
    map.put("Bob", 20f);
    map.put("John", 30f);
    map.put("Mary", 45f);

    BalanceRestaurantBill solution = new BalanceRestaurantBill();

    System.out.println(solution.transactionsDescription(map));
  }

  List<String> transactionsDescription(Map<String, Float> transactionsMap) {
    return transactionHelper(debtCalculator(transactionsMap));
  }

  Map<String, Float> debtCalculator(Map<String, Float> transactionsMap) {
    float totalValueOfBill = 0f;

    // Calculate the total value paid by all the participants
    for(Entry entry : transactionsMap.entrySet()) {
      totalValueOfBill += transactionsMap.get(entry.getKey());
    }

    // Calculate what value each participant has to pay
    // so that all pay the same amount
    float individualValueToBePaid = totalValueOfBill / transactionsMap.size();

    Map<String, Float> debtMap = new HashMap<>();

    // Compute the amount that each participant needs to receive or to distribute by the others
    // Negative values means that participants paid less than they should and need to distribute money
    // Positive values means that participants paid more than they should and need to receive money
    // (Alice, 5), (Bob, 10), (John, 0), (Mary, -15)
    for(Entry entry : transactionsMap.entrySet()) {
      float individualDebt = individualValueToBePaid - (float) entry.getValue();

      if(individualDebt != 0) {
        debtMap
            .put(entry.getKey().toString(), individualDebt);
      }
    }

    return debtMap;
  }

  List<String> transactionHelper(Map<String, Float> debtMap) {
    List<String> transactionsDescription = new ArrayList();

    // If positive, go through other map entries until a negative one is found
    // When a negative value is found, make the necessary verifications, compute the result and update map
    for(String keyDebtor : debtMap.keySet()) {
      float valueInDebt = debtMap.get(keyDebtor);

      if(valueInDebt > 0) {
        for(String keyReceiver : debtMap.keySet()) {
          float valueToReceive = debtMap.get(keyReceiver);

          if(!keyReceiver.equals(keyDebtor)
              && valueToReceive < 0) {
            float transaction = 0;

            if(valueInDebt <= valueToReceive * -1 && valueInDebt != 0) {
              transaction = valueInDebt;
              valueInDebt = 0;
              valueToReceive += transaction;
            } else if(valueInDebt > valueToReceive * -1 && valueInDebt != 0) {
              transaction = valueToReceive * -1;
              valueInDebt -= valueToReceive * -1;
              valueToReceive = 0;
            }

            debtMap.put(keyDebtor, valueInDebt);
            debtMap.put(keyReceiver, valueToReceive);

            if(transaction != 0) {
              transactionsDescription
                  .add(keyDebtor + " to " + keyReceiver + " -> " + transaction + "\n");
            }
          }
        }
      }
    }

    return transactionsDescription;
  }
}
