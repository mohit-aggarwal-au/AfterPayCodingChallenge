# AfterPayCodingChallenge


problem statement is given in the pdf file - coding-exercise.pdf

Some details about the program - 

1. Program has been designed to consume transactions not necessarily in chronological order.
2. For the sake of simplicity, Spring is not used.
3. Code coverage stands at 100% for branch and 92% for line

Assumptions - 
1. If a single transaction amount is more than threshold amount, that transaction will be considered as fraudulent.
2. There may be duplicate hashed card numbers which has been detected as fraudulent over multiple 24 hours sliding window periods, program will return only distinct hashed card values.
3. Sliding window is a time period that stretches back in time from the present. For instance, a sliding window of two minutes indicate that any events that have occurred in the past 2 minutes. As events fall out of the sliding time window, they will no longer match against rules using this particular sliding window.
4. Transaction details are to be read from a csv file. If validation fails while parsing csv file, then program will throw exception and not proceed further. This behavior can be changed by ignoring the data row with failed validation, however, in practical terms, I believe we should get to the root cause of the invalid data.

Further improvements - 

1. Use of spring to use dependencies, use of component test cases to check integration of spring components.
2. Spring environment specific application properties can be used to read Threshold value and other properties such as sliding time window duration.
3. Line and branch code coverage can be enforced using jacocoTestCoverageVerification.
4. All the validation failure details in input csv file can be collected and returned back for further analysis.
5. Lombok annotations can be used to remove getter and setters.
 
