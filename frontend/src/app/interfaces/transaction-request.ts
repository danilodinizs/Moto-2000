import { TransactionPaymentMethod } from "../enums/transaction-payment-method";
import { TransactionItemRequest } from "./transaction-item-request";

export interface TransactionRequest {
  items: TransactionItemRequest[];
  description: string;
  note?: string;
  transactionPaymentMethod: TransactionPaymentMethod;
  serviceOrderIds?: string[];
  clientId: string;
}
