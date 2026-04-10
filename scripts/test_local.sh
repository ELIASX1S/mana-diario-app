#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT_DIR"

chmod +x gradlew

./gradlew :app:assembleDebug
./gradlew testDebugUnitTest

echo "✅ Build e testes unitários concluídos com sucesso."
