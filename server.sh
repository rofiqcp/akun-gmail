#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="/root/otomasi/gmail"
BACKEND_DIR="$ROOT_DIR/backend-spring"
FRONTEND_DIR="$ROOT_DIR/frontend-angular"
RUN_DIR="$ROOT_DIR/.run"
BACKEND_PID_FILE="$RUN_DIR/backend.pid"
FRONTEND_PID_FILE="$RUN_DIR/frontend.pid"
BACKEND_LOG="$RUN_DIR/backend.log"
FRONTEND_LOG="$RUN_DIR/frontend.log"
BACKEND_JAR="$BACKEND_DIR/target/gmail-backend-1.0.0.jar"

mkdir -p "$RUN_DIR"

start_backend() {
  if [[ -f "$BACKEND_PID_FILE" ]] && kill -0 "$(cat "$BACKEND_PID_FILE")" 2>/dev/null; then
    echo "Backend already running (PID $(cat "$BACKEND_PID_FILE"))"
    return 0
  fi
  if [[ ! -f "$BACKEND_JAR" ]]; then
    echo "Backend jar not found. Build first: mvn clean package -DskipTests"
    return 1
  fi
  nohup java -jar "$BACKEND_JAR" > "$BACKEND_LOG" 2>&1 &
  echo $! > "$BACKEND_PID_FILE"
  echo "Backend started (PID $(cat "$BACKEND_PID_FILE"))"
}

start_frontend() {
  if [[ -f "$FRONTEND_PID_FILE" ]] && kill -0 "$(cat "$FRONTEND_PID_FILE")" 2>/dev/null; then
    echo "Frontend already running (PID $(cat "$FRONTEND_PID_FILE"))"
    return 0
  fi
  if [[ ! -f "$FRONTEND_DIR/package.json" ]]; then
    echo "Frontend not found. Please ensure Angular project exists."
    return 1
  fi
  # Kill any process using port 4200
  pkill -f "ng serve" 2>/dev/null || true
  fuser -k 4200/tcp 2>/dev/null || true
  sleep 2
  (cd "$FRONTEND_DIR" && nohup npm start > "$FRONTEND_LOG" 2>&1 &)
  FRONTEND_NEW_PID=$!
  echo $FRONTEND_NEW_PID > "$FRONTEND_PID_FILE"
  sleep 3
  # Verify the process is still running
  if kill -0 $FRONTEND_NEW_PID 2>/dev/null; then
    echo "Frontend started (PID $FRONTEND_NEW_PID)"
  else
    echo "Frontend failed to start. Check logs: tail -f $FRONTEND_LOG"
    cat "$FRONTEND_LOG"
    rm -f "$FRONTEND_PID_FILE"
    return 1
  fi
}

stop_backend() {
  if [[ -f "$BACKEND_PID_FILE" ]] && kill -0 "$(cat "$BACKEND_PID_FILE")" 2>/dev/null; then
    kill "$(cat "$BACKEND_PID_FILE")" || true
    rm -f "$BACKEND_PID_FILE"
    echo "Backend stopped"
  else
    echo "Backend not running"
  fi
}

stop_frontend() {
  if [[ -f "$FRONTEND_PID_FILE" ]] && kill -0 "$(cat "$FRONTEND_PID_FILE")" 2>/dev/null; then
    kill "$(cat "$FRONTEND_PID_FILE")" || true
    rm -f "$FRONTEND_PID_FILE"
    echo "Frontend stopped"
  else
    echo "Frontend not running"
  fi
}

status_backend() {
  if [[ -f "$BACKEND_PID_FILE" ]] && kill -0 "$(cat "$BACKEND_PID_FILE")" 2>/dev/null; then
    echo "Backend running (PID $(cat "$BACKEND_PID_FILE"))"
  else
    echo "Backend not running"
  fi
}

status_frontend() {
  if [[ -f "$FRONTEND_PID_FILE" ]] && kill -0 "$(cat "$FRONTEND_PID_FILE")" 2>/dev/null; then
    echo "Frontend running (PID $(cat "$FRONTEND_PID_FILE"))"
  else
    echo "Frontend not running"
  fi
}

case "${1:-menu}" in
  start)
    start_backend
    start_frontend
    ;;
  stop)
    stop_frontend
    stop_backend
    ;;
  restart)
    stop_frontend
    stop_backend
    start_backend
    start_frontend
    ;;
  status)
    status_backend
    status_frontend
    ;;
  menu)
    echo ""
    echo "========================================="
    echo "   Gmail Automation Server Management    "
    echo "========================================="
    echo ""
    echo "  1. Start   (start backend & frontend)"
    echo "  2. Stop    (stop services)"
    echo "  3. Restart (restart services)"
    echo "  4. Status  (check status)"
    echo ""
    echo "========================================="
    read -p "Pilih option (1-4): " choice
    
    case "$choice" in
      1)
        start_backend
        start_frontend
        ;;
      2)
        stop_frontend
        stop_backend
        ;;
      3)
        stop_frontend
        stop_backend
        start_backend
        start_frontend
        ;;
      4)
        status_backend
        status_frontend
        ;;
      *)
        echo "Option tidak valid! Pilih 1-4"
        exit 1
        ;;
    esac
    ;;
  *)
    echo "Usage: $0 {start|stop|restart|status}"
    exit 1
    ;;
esac
