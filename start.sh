#!/usr/bin/env bash
set -e
ROOT="$(cd "$(dirname "$0")" && pwd)"
JAVA17_HOME="/usr/lib/jvm/java-17-openjdk-amd64"
BACKEND_PORT=8090
FRONTEND_PORT=3000
SPRING_PROFILE="${SPRING_PROFILE:-local}"
echo "╔══════════════════════════════════════════╗"
echo "║         SpringERP Launcher               ║"
echo "╚══════════════════════════════════════════╝"
echo ""
# ── 1. Build backend ─────────────────────────────────────────────
echo "▶ Building Backend (skip tests)..."
cd "$ROOT/backend"
JAVA_HOME="$JAVA17_HOME" mvn package -DskipTests -q
echo "  Build complete."
# ── 2. Start backend ─────────────────────────────────────────────
echo "▶ Starting Backend on port $BACKEND_PORT..."
"$JAVA17_HOME/bin/java" \
  -jar target/spring-erp.jar \
  --spring.profiles.active="$SPRING_PROFILE" \
  --server.port="$BACKEND_PORT" \
  --jwt.secret=springerpsecretkeywith512bitsforhs512algorithm12345678901234 \
  --app.jwt.expiration-milliseconds=86400000 \
  -Xms256m -Xmx512m &
BACKEND_PID=$!
echo "  Backend PID: $BACKEND_PID"
# ── 3. Start frontend ─────────────────────────────────────────────
echo "▶ Starting Frontend on port $FRONTEND_PORT..."
cd "$ROOT/frontend"
VITE_API_BASE_URL="http://localhost:$BACKEND_PORT/api/v1" \
  npm run dev -- --host 0.0.0.0 --port "$FRONTEND_PORT" &
FRONTEND_PID=$!
echo "  Frontend PID: $FRONTEND_PID"
echo ""
echo "  ┌────────────────────────────────────────────────────────┐"
echo "  │  Frontend:    http://localhost:$FRONTEND_PORT                  │"
echo "  │  Backend API: http://localhost:$BACKEND_PORT/api/v1           │"
echo "  │  Swagger UI:  http://localhost:$BACKEND_PORT/api/v1/swagger-ui.html │"
echo "  │  H2 Console:  http://localhost:$BACKEND_PORT/api/v1/h2-console │"
echo "  │  Login:       admin@springerp.com / Admin@123          │"
echo "  └────────────────────────────────────────────────────────┘"
echo ""
echo "  Press Ctrl+C to stop all services."
# ── Cleanup on Ctrl+C / exit ──────────────────────────────────────
cleanup() {
  echo ""
  echo "Stopping services (PID $BACKEND_PID, $FRONTEND_PID)..."
  kill "$BACKEND_PID" "$FRONTEND_PID" 2>/dev/null || true
  echo "Done."
}
trap cleanup EXIT INT TERM
wait
