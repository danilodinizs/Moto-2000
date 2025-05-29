import { TransactionPaymentMethod } from "../enums/transaction-payment-method";
import { TransactionStatus } from "../enums/transaction-status";
import { TransactionType } from "../enums/transaction-type";
import { Client } from "./client";
import { ServiceOrder } from "./service-order";
import { TransactionItemRequest } from "./transaction-item-request";

export interface Transaction {
  id: string;
  totalProducts: number;
  totalPrice: number;
  transactionType: TransactionType;
  transactionPaymentMethod: TransactionPaymentMethod;
  transactionStatus: TransactionStatus;
  description: string;
  note?: string;
  updatedAt?: string;
  createdAt: string;
  client: Client;
  items: TransactionItemRequest[];
  serviceOrder: ServiceOrder[];
}
