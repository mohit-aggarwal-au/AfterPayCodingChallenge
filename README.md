# AfterPayCodingChallenge

Assumptions - 
1. If a single transaction amount is more than threshold amount, that transaction will be considered as fraudulent
2. There may be duplicate hashed card numbers which were detected as fraudulent over multiple 24 hours sliding window periods, program will return only distinct hashed card values 
3. Sliding window is a time period that stretches back in time from the present. For instance, a sliding window of two minutes indicate that any events that have occurred in the past 2 minutes. As events fall out of the sliding time window, they will no longer match against rules using this particular sliding window.
4. Transaction details are to be read from a csv file. If validation fails while parsing csv file, then program will throw exception and not proceed further. This behavior can be changed by ignoring the data row with failed validation, however, in practical terms, I believe we should get to the root cause of the invalid data.

Further improvements - 

1. Use of spring to use dependencies
2. Use of lombok for getter and setter methods
 