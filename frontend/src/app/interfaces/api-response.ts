export interface ApiResponse<T> {
  status: number;
  message: string;
  data: T;
  token?: string;
  role?: string;
  expirationTime?: string;
  totalPages?: number;
  totalElements?: number;
  timeStamp: string;
}
