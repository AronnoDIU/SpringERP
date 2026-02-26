import apiClient from './client';
import type { LoginRequest, JwtResponse } from '../types';
export const authApi = {
  login: (data: LoginRequest) =>
    apiClient.post<JwtResponse>('/auth/login', data).then((r) => r.data),
  logout: () =>
    apiClient.post<string>('/auth/logout').then((r) => r.data),
  refreshToken: () =>
    apiClient.post<JwtResponse>('/auth/refresh-token').then((r) => r.data),
};
