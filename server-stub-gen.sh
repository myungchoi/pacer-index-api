#*******************************************************************************
# Copyright (c) 2019 Georgia Tech Research Institute
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#*******************************************************************************
java -jar ../../Tools/openapi/swagger-codegen-cli.jar generate -i swagger.json -l spring --api-package edu.gatech.chai.pacer.api --model-package edu.gatech.chai.pacer.model --group-id edu.gatech.chai --artifact-id pacer-index-api --artifact-version 0.0.1-SNAPSHOT -o ./
